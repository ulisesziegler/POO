import java.util.ArrayList;
import java.util.List;

public class Archivos {

    private final List<misas> listaMisas = new ArrayList<>();

    public List<misas> getmisass() {
        return new ArrayList<>(listaMisas);
    }

    public misas buscarPorFecha(String fecha) {
        if (fecha == null) {
            return null;
        }

        for (misas misa : listaMisas) {
            if (misa.getfecha().equals(fecha.trim())) {
                return misa;
            }
        }

        return null;
    }

    public boolean agregarmisas(misas misa) {
        if (misa == null ||
                buscarPorFecha(misa.getfecha()) != null) {
            return false;
        }

        listaMisas.add(misa);
        return true;
    }

    public boolean eliminarmisas(String fecha) {
        misas misa = buscarPorFecha(fecha);

        if (misa == null) {
            return false;
        }

        return listaMisas.remove(misa);
    }

    public boolean editarmisas(
            String fechaOriginal,
            String basilica,
            String provincia,
            String ciudad) {

        misas misa = buscarPorFecha(fechaOriginal);

        if (misa == null) {
            return false;
        }

        // Validar los datos antes de modificar el objeto.
        misas validacion = new misas(
                basilica, provincia, ciudad, fechaOriginal
        );

        misa.setbasilica(validacion.getbasilica());
        misa.setprovincia(validacion.getprovincia());
        misa.setciudad(validacion.getciudad());

        return true;
    }

    public List<misas> buscar(String criterio, String texto) {
        List<misas> resultado = new ArrayList<>();

        if (criterio == null || texto == null) {
            return resultado;
        }

        String busqueda = texto.trim().toLowerCase();

        for (misas misa : listaMisas) {
            String campo;

            switch (criterio) {
                case "Basilica":
                case "Basílica":
                    campo = misa.getbasilica();
                    break;
                case "Provincia":
                    campo = misa.getprovincia();
                    break;
                case "Ciudad":
                    campo = misa.getciudad();
                    break;
                case "Fecha":
                    campo = misa.getfecha();
                    break;
                default:
                    continue;
            }

            if (campo.toLowerCase().contains(busqueda)) {
                resultado.add(misa);
            }
        }

        return resultado;
    }

    public void cargarmisasDesdeArchivo(misas misa) {
        agregarmisas(misa);
    }
}