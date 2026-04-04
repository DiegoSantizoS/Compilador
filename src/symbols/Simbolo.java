package symbols; 

public class Simbolo {
    private String nombre;
    private String tipo;
    private int linea;

    public Simbolo(String nombre, String tipo, int linea) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.linea = linea;
    }

    @Override
    public String toString() {
        return String.format("%-15s | %-10s | %d", nombre, tipo, linea);
    }
    
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getLinea() { return linea; }
}