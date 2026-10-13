import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class Main extends JFrame {

    private final Archivos archivos = new Archivos();

    public Main() {
        super("Sistema de Gestión de Misas");

        try {
            Persistencia.cargar(archivos);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudieron cargar los archivos: " + e.getMessage(),
                    "Error de lectura",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(650, 390);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        JLabel titulo = new JLabel(
                "SISTEMA DE GESTIÓN DE MISAS",
                SwingConstants.CENTER
        );
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(
                BorderFactory.createEmptyBorder(22, 10, 12, 10)
        );
        add(titulo, BorderLayout.NORTH);

        JPanel botones = new JPanel(new GridLayout(2, 2, 12, 12));
        botones.setBorder(
                BorderFactory.createEmptyBorder(12, 35, 25, 35)
        );

        agregarBoton(botones, "Buscar misas", e -> ventanaBuscar());
        agregarBoton(botones, "Administrar misas",
                e -> ventanaAdministracion());

        add(botones, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                guardarYSalir();
            }
        });
    }

    private void agregarBoton(
            JPanel panel,
            String texto,
            java.awt.event.ActionListener accion) {

        JButton boton = new JButton(texto);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        boton.addActionListener(accion);
        panel.add(boton);
    }

    private void guardarYSalir() {
        try {
            Persistencia.guardar(archivos);
            dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudieron guardar los datos: " + e.getMessage(),
                    "Error de escritura",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void ventanaBuscar() {
        JDialog dialogo = new JDialog(this, "Buscar misas", true);

        dialogo.setSize(760, 420);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout(8, 8));

        JPanel superior = new JPanel(new FlowLayout());

        JComboBox<String> criterio = new JComboBox<>(
                new String[]{
                        "Fecha",
                        "Basilica",
                        "Provincia",
                        "Ciudad"
                }
        );

        JTextField texto = new JTextField(22);
        JButton buscar = new JButton("Buscar");

        superior.add(new JLabel("Buscar por:"));
        superior.add(criterio);
        superior.add(texto);
        superior.add(buscar);

        dialogo.add(superior, BorderLayout.NORTH);

        DefaultTableModel modelo = modeloTabla();
        JTable tabla = new JTable(modelo);

        dialogo.add(new JScrollPane(tabla), BorderLayout.CENTER);

        buscar.addActionListener(e -> {
            String campo = (String) criterio.getSelectedItem();
            String valor = texto.getText();

            llenarTabla(
                    modelo,
                    archivos.buscar(campo, valor)
            );
        });

        llenarTabla(modelo, archivos.getmisass());

        dialogo.setVisible(true);
    }

    private void ventanaAdministracion() {
        JDialog dialogo = new JDialog(
                this,
                "Administración de misas",
                true
        );

        dialogo.setSize(800, 440);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout(8, 8));

        DefaultTableModel modelo = modeloTabla();
        JTable tabla = new JTable(modelo);

        dialogo.add(new JScrollPane(tabla), BorderLayout.CENTER);

        llenarTabla(modelo, archivos.getmisass());

        JPanel acciones = new JPanel(new FlowLayout());

        JButton agregar = new JButton("Agregar misa");
        JButton editar = new JButton("Editar misa");
        JButton eliminar = new JButton("Eliminar misa");
        JButton actualizar = new JButton("Actualizar calendario");

        acciones.add(agregar);
        acciones.add(editar);
        acciones.add(eliminar);
        acciones.add(actualizar);

        dialogo.add(acciones, BorderLayout.SOUTH);

        // AGREGAR MISA
        agregar.addActionListener(e -> {
            String[] datos = formularioMisa(dialogo, null);

            if (datos != null) {
                try {
                    misas misa = new misas(
                            datos[0],
                            datos[1],
                            datos[2],
                            datos[3]
                    );

                    if (!archivos.agregarmisas(misa)) {
                        JOptionPane.showMessageDialog(
                                dialogo,
                                "Ya existe una misa en esa fecha."
                        );
                    }

                    llenarTabla(modelo, archivos.getmisass());

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            dialogo,
                            "La fecha debe tener un formato válido."
                    );
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(
                            dialogo,
                            ex.getMessage()
                    );
                }
            }
        });

        // EDITAR MISA
        editar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();

            if (fila < 0) {
                JOptionPane.showMessageDialog(
                        dialogo,
                        "Seleccione una misa de la tabla."
                );
                return;
            }

            fila = tabla.convertRowIndexToModel(fila);

            String fechaOriginal = String.valueOf(
                    modelo.getValueAt(fila, 3)
            );

            misas misa = archivos.buscarPorFecha(fechaOriginal);

            if (misa == null) {
                JOptionPane.showMessageDialog(
                        dialogo,
                        "No se encontró la misa seleccionada."
                );
                return;
            }

            String[] datos = formularioMisa(dialogo, misa);

            if (datos != null) {
                try {
                    boolean ok = archivos.editarmisas(
                            fechaOriginal,
                            datos[0],
                            datos[1],
                            datos[2]
                    );

                    if (!ok) {
                        JOptionPane.showMessageDialog(
                                dialogo,
                                "No se pudo editar la misa."
                        );
                    }

                    llenarTabla(modelo, archivos.getmisass());

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(
                            dialogo,
                            ex.getMessage()
                    );
                }
            }
        });

        // ELIMINAR MISA
        eliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();

            if (fila < 0) {
                JOptionPane.showMessageDialog(
                        dialogo,
                        "Seleccione una misa de la tabla."
                );
                return;
            }

            fila = tabla.convertRowIndexToModel(fila);

            String fecha = String.valueOf(
                    modelo.getValueAt(fila, 3)
            );

            int confirmacion = JOptionPane.showConfirmDialog(
                    dialogo,
                    "¿Eliminar la misa de la fecha " + fecha + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean eliminada = archivos.eliminarmisas(fecha);

                if (!eliminada) {
                    JOptionPane.showMessageDialog(
                            dialogo,
                            "No se pudo eliminar la misa."
                    );
                }

                llenarTabla(modelo, archivos.getmisass());
            }
        });

        // ACTUALIZAR TABLA
        actualizar.addActionListener(e ->
                llenarTabla(modelo, archivos.getmisass())
        );

        dialogo.setVisible(true);
    }

    private String[] formularioMisa(Component padre, misas misa) {
        JTextField basilica = new JTextField(
                misa == null ? "" : misa.getbasilica()
        );

        JTextField provincia = new JTextField(
                misa == null ? "" : misa.getprovincia()
        );

        JTextField ciudad = new JTextField(
                misa == null ? "" : misa.getciudad()
        );

        JTextField fecha = new JTextField(
                misa == null ? "" : misa.getfecha()
        );

        if (misa != null) {
            fecha.setEditable(false);
        }

        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));

        panel.add(new JLabel("Basílica:"));
        panel.add(basilica);

        panel.add(new JLabel("Provincia:"));
        panel.add(provincia);

        panel.add(new JLabel("Ciudad:"));
        panel.add(ciudad);

        panel.add(new JLabel("Fecha (AAAAMMDD):"));
        panel.add(fecha);

        int opcion = JOptionPane.showConfirmDialog(
                padre,
                panel,
                misa == null ? "Nueva misa" : "Editar misa",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcion != JOptionPane.OK_OPTION) {
            return null;
        }

        return new String[]{
                basilica.getText().trim(),
                provincia.getText().trim(),
                ciudad.getText().trim(),
                fecha.getText().trim()
        };
    }

    private DefaultTableModel modeloTabla() {
        return new DefaultTableModel(
                new Object[]{
                        "Basílica",
                        "Provincia",
                        "Ciudad",
                        "Fecha"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    private void llenarTabla(
            DefaultTableModel modelo,
            List<misas> listaMisas) {

        modelo.setRowCount(0);

        for (misas misa : listaMisas) {
            modelo.addRow(new Object[]{
                    misa.getbasilica(),
                    misa.getprovincia(),
                    misa.getciudad(),
                    misa.getfecha()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main ventana = new Main();
            ventana.setVisible(true);
        });
    }
}
