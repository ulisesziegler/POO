import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class misas {

    private String basilica;
    private String provincia;
    private String ciudad;
    private String fecha;

    public misas(String basilica, String provincia,
                 String ciudad, String fecha) {

        setbasilica(basilica);
        setprovincia(provincia);
        setciudad(ciudad);
        setfecha(fecha);
    }

    public String getbasilica() {
        return basilica;
    }

    public String getprovincia() {
        return provincia;
    }

    public String getciudad() {
        return ciudad;
    }

    public String getfecha() {
        return fecha;
    }

    public void setbasilica(String basilica) {
        if (basilica == null || basilica.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La basílica no puede estar vacía."
            );
        }
        this.basilica = basilica.trim();
    }

    public void setprovincia(String provincia) {
        if (provincia == null || provincia.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La provincia no puede estar vacía."
            );
        }
        this.provincia = provincia.trim();
    }

    public void setciudad(String ciudad) {
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La ciudad no puede estar vacía."
            );
        }
        this.ciudad = ciudad.trim();
    }

    public void setfecha(String fecha) {
        if (fecha == null || !fecha.matches("\\d{8}")) {
            throw new IllegalArgumentException(
                    "La fecha debe tener el formato AAAAMMDD."
            );
        }

        try {
            LocalDate.parse(
                    fecha,
                    DateTimeFormatter.BASIC_ISO_DATE
            );
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "La fecha no es válida."
            );
        }

        this.fecha = fecha;
    }
}