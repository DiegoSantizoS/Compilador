package CodigoMaquina;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class PanelCodigoMaquina extends JPanel {

    private static final Color PANEL_BG    = new Color(30, 37, 40);
    private static final Color TITLE_COLOR = new Color(221, 230, 234);
    private static final Color TEXT_COLOR  = new Color(231, 238, 241);
    private static final Color BTN_COMPILE = new Color(0, 122, 204);
    private static final Color BTN_RUN     = new Color(0, 160, 80);

    private final JTextArea areaASM;
    private final JButton   btnCompilar;
    private final JButton   btnEjecutar;
    private final JLabel    lblStatus;

    // Callback called on the EDT with (text, color) for each output line.
    // Color.WHITE = normal, Color.GREEN = success, Color.RED = error.
    private BiConsumer<String, Color> outputCallback;

    private Path exePath = null;

    public PanelCodigoMaquina() {
        setLayout(new BorderLayout());
        setBackground(PANEL_BG);

        // ── North: title + buttons ──────────────────────────────────────────
        JPanel north = new JPanel();
        north.setBackground(PANEL_BG);
        north.setLayout(new BoxLayout(north, BoxLayout.X_AXIS));
        north.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        JLabel titulo = new JLabel("Código Ensamblador x86-32 (NASM)", SwingConstants.LEFT);
        titulo.setForeground(TITLE_COLOR);
        titulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        btnCompilar = makeButton("Compilar a .EXE", BTN_COMPILE);
        btnEjecutar = makeButton("Ejecutar .EXE",   BTN_RUN);
        btnEjecutar.setEnabled(false);

        lblStatus = new JLabel("");
        lblStatus.setForeground(new Color(180, 220, 180));
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        north.add(titulo);
        north.add(Box.createHorizontalGlue());
        north.add(lblStatus);
        north.add(Box.createHorizontalStrut(12));
        north.add(btnCompilar);
        north.add(Box.createHorizontalStrut(6));
        north.add(btnEjecutar);
        add(north, BorderLayout.NORTH);

        // ── Center: ASM code view ───────────────────────────────────────────
        areaASM = new JTextArea();
        areaASM.setEditable(false);
        areaASM.setBackground(PANEL_BG);
        areaASM.setForeground(TEXT_COLOR);
        areaASM.setCaretColor(TEXT_COLOR);
        areaASM.setFont(new Font("Consolas", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(areaASM);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(PANEL_BG);
        scroll.setBackground(PANEL_BG);
        add(scroll, BorderLayout.CENTER);

        btnCompilar.addActionListener(e -> compilar());
        btnEjecutar.addActionListener(e -> ejecutar());
    }

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Called with the already-optimized TAC string to generate and display ASM.
     */
    public void showASM(String codigoTACOptimizado) {
        exePath = null;
        btnEjecutar.setEnabled(false);
        lblStatus.setText("");

        GeneradorASM gen = new GeneradorASM();
        String asm = gen.generar(codigoTACOptimizado);

        areaASM.setText(asm);
        areaASM.setCaretPosition(0);
        areaASM.setFocusable(false);
        revalidate();
        repaint();
    }

    public String getASM() { return areaASM.getText(); }

    /**
     * The main frame sets this so compile/run output goes to jTabbedPaneTerminal.
     * Called on the EDT with (message, color).
     */
    public void setOutputCallback(BiConsumer<String, Color> cb) {
        this.outputCallback = cb;
    }

    // ── Compile ───────────────────────────────────────────────────────────────

    private void compilar() {
        String asm = areaASM.getText().trim();
        if (asm.isEmpty()) {
            emit("No hay código ensamblador. Presiona Run primero.", Color.RED);
            return;
        }

        btnCompilar.setEnabled(false);
        btnEjecutar.setEnabled(false);
        lblStatus.setText("Compilando...");
        lblStatus.setForeground(new Color(200, 200, 100));
        emit("", Color.WHITE);
        emit("=== Generación de Código Máquina ===", Color.WHITE);

        new javax.swing.SwingWorker<Boolean, Object[]>() {
            Path asmFile, objFile, exeFile;

            @Override
            protected Boolean doInBackground() throws Exception {
                Path tmpDir = Path.of(System.getProperty("java.io.tmpdir"), "compilador_umg");
                Files.createDirectories(tmpDir);
                asmFile = tmpDir.resolve("salida.asm");
                objFile = tmpDir.resolve("salida.obj");

                // If the previous exe is still locked, use a fresh name
                Path candidate = tmpDir.resolve("salida.exe");
                try {
                    Files.deleteIfExists(candidate);
                    exeFile = candidate;
                } catch (java.io.IOException ignored) {
                    exeFile = tmpDir.resolve("salida_" + System.currentTimeMillis() + ".exe");
                }

                Files.writeString(asmFile, asm);
                publish(msg("Archivo ASM: " + asmFile, Color.WHITE));

                // NASM
                String nasmCmd = findTool("nasm");
                if (nasmCmd == null) {
                    publish(msg("[ERROR] NASM no encontrado en PATH.", Color.RED));
                    publish(msg("  Descarga NASM en: https://www.nasm.us/", Color.RED));
                    publish(msg("  Agrega la carpeta de NASM al PATH del sistema.", Color.RED));
                    return false;
                }
                boolean nasmOk = runProcess(
                    new String[]{nasmCmd, "-f", "win64", asmFile.toString(), "-o", objFile.toString()},
                    "NASM"
                );
                if (!nasmOk) return false;

                // GCC/MinGW link (64-bit)
                String gccCmd = findTool("gcc");
                if (gccCmd == null) {
                    publish(msg("[ERROR] GCC (MinGW) no encontrado en PATH.", Color.RED));
                    publish(msg("  Instala MinGW-w64 y agrega bin/ al PATH.", Color.RED));
                    return false;
                }
                boolean gccOk = runProcess(
                    new String[]{gccCmd, objFile.toString(), "-o", exeFile.toString()},
                    "GCC"
                );
                if (gccOk) {
                    exePath = exeFile;
                    publish(msg("Ejecutable generado: " + exeFile, new Color(100, 230, 100)));
                }
                return gccOk;
            }

            private boolean runProcess(String[] cmd, String tool)
                    throws IOException, InterruptedException {
                publish(msg("[" + tool + "] " + String.join(" ", cmd), Color.WHITE));
                ProcessBuilder pb = new ProcessBuilder(cmd);
                pb.redirectErrorStream(true);
                Process p = pb.start();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(p.getInputStream()))) {
                    String line;
                    while ((line = br.readLine()) != null)
                        publish(msg("  " + line, Color.WHITE));
                }
                int code = p.waitFor();
                if (code != 0) {
                    publish(msg("[" + tool + "] Falló con código: " + code, Color.RED));
                    return false;
                }
                publish(msg("[" + tool + "] OK", new Color(100, 230, 100)));
                return true;
            }

            private String findTool(String name) {
                try {
                    ProcessBuilder pb = new ProcessBuilder("where", name);
                    pb.redirectErrorStream(true);
                    Process p = pb.start();
                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(p.getInputStream()))) {
                        String line = br.readLine();
                        if (line != null && !line.isBlank() && p.waitFor() == 0) return name;
                    }
                } catch (Exception ignored) {}

                for (String candidate : new String[]{
                    "C:\\Program Files\\NASM\\" + name + ".exe",
                    "C:\\NASM\\" + name + ".exe",
                    "C:\\MinGW\\bin\\" + name + ".exe",
                    "C:\\MinGW32\\bin\\" + name + ".exe",
                    "C:\\msys64\\mingw32\\bin\\" + name + ".exe",
                    "C:\\msys64\\ucrt64\\bin\\"  + name + ".exe"
                }) {
                    if (new File(candidate).exists()) return candidate;
                }
                return null;
            }

            // Pack (text,color) into an Object[] for publish
            private Object[] msg(String text, Color color) { return new Object[]{text, color}; }

            @Override
            protected void process(java.util.List<Object[]> chunks) {
                for (Object[] pair : chunks)
                    emit((String) pair[0], (Color) pair[1]);
            }

            @Override
            protected void done() {
                btnCompilar.setEnabled(true);
                try {
                    boolean ok = get();
                    if (ok) {
                        lblStatus.setText("EXE listo");
                        lblStatus.setForeground(new Color(100, 230, 100));
                        btnEjecutar.setEnabled(true);
                    } else {
                        lblStatus.setText("Error al compilar");
                        lblStatus.setForeground(new Color(230, 100, 100));
                    }
                } catch (Exception ex) {
                    lblStatus.setText("Error interno");
                    emit("[ERROR] " + ex.getMessage(), Color.RED);
                }
            }
        }.execute();
    }

    // ── Execute ───────────────────────────────────────────────────────────────

    private void ejecutar() {
        if (exePath == null || !Files.exists(exePath)) {
            emit("No hay ejecutable. Compila primero.", Color.RED);
            return;
        }

        btnEjecutar.setEnabled(false);
        lblStatus.setText("Ejecutando...");
        emit("", Color.WHITE);
        emit("--- Salida del programa ---", new Color(200, 200, 100));

        new javax.swing.SwingWorker<Integer, Object[]>() {
            @Override
            protected Integer doInBackground() throws Exception {
                ProcessBuilder pb = new ProcessBuilder(exePath.toString());
                pb.redirectErrorStream(true);
                Process p = pb.start();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(p.getInputStream()))) {
                    String line;
                    while ((line = br.readLine()) != null)
                        publish(new Object[]{line, Color.WHITE});
                }
                boolean finished = p.waitFor(30, TimeUnit.SECONDS);
                if (!finished) {
                    p.destroyForcibly();
                    publish(new Object[]{"[Timeout: el proceso excedió 30 s y fue terminado]", Color.RED});
                    return -1;
                }
                return p.exitValue();
            }

            @Override
            protected void process(java.util.List<Object[]> chunks) {
                for (Object[] pair : chunks) emit((String) pair[0], (Color) pair[1]);
            }

            @Override
            protected void done() {
                btnEjecutar.setEnabled(true);
                try {
                    int code = get();
                    Color c = code == 0 ? new Color(100, 230, 100) : new Color(230, 100, 100);
                    emit("--- Programa terminó con código: " + code + " ---", c);
                    lblStatus.setText(code == 0 ? "Ejecutado OK" : "Terminó con error");
                    lblStatus.setForeground(c);
                } catch (Exception ex) {
                    emit("[ERROR al ejecutar] " + ex.getMessage(), Color.RED);
                    lblStatus.setText("Error al ejecutar");
                }
            }
        }.execute();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Sends a line to the callback (if set) or silently ignores it. */
    private void emit(String text, Color color) {
        if (outputCallback != null) outputCallback.accept(text, color);
    }

    private JButton makeButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 26));
        btn.setMaximumSize(new Dimension(130, 26));
        return btn;
    }
}
