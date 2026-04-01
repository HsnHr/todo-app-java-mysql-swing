import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GUI {

    private JFrame frame;
    private JPanel anaPanel;
    private JTextField gorevAlani;
    private JTextField aramaAlani;
    private JCheckBox sadeceBekleyenler;
    private JTable table;
    private DefaultTableModel tableModel;

    private JButton ekleBtn;
    private JButton yenileBtn;
    private JButton tamamlaBtn;
    private JButton silBtn;
    private JButton temaBtn;

    private JLabel baslik;
    private JLabel araLabel;

    private boolean darkMode = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI().olustur());
    }

    public void olustur() {
        frame = new JFrame("To-Do App");
        frame.setSize(950, 560);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        anaPanel = new JPanel(new BorderLayout(15, 15));
        anaPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        baslik = new JLabel("To-Do Task Manager", SwingConstants.CENTER);
        baslik.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel ustPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        ustPanel.setOpaque(false);

        JPanel eklemePaneli = new JPanel(new BorderLayout(10, 10));
        eklemePaneli.setOpaque(false);

        gorevAlani = new JTextField();
        gorevAlani.setFont(new Font("Arial", Font.PLAIN, 15));

        ekleBtn = new JButton("Görev Ekle");
        stilButon(ekleBtn, new Color(52, 152, 219));

        eklemePaneli.add(gorevAlani, BorderLayout.CENTER);
        eklemePaneli.add(ekleBtn, BorderLayout.EAST);

        JPanel filtrePaneli = new JPanel(new BorderLayout(10, 10));
        filtrePaneli.setOpaque(false);

        aramaAlani = new JTextField();
        aramaAlani.setFont(new Font("Arial", Font.PLAIN, 15));

        araLabel = new JLabel("Ara:");

        sadeceBekleyenler = new JCheckBox("Sadece bekleyen görevler");
        sadeceBekleyenler.setOpaque(false);
        sadeceBekleyenler.setFont(new Font("Arial", Font.PLAIN, 14));

        filtrePaneli.add(araLabel, BorderLayout.WEST);
        filtrePaneli.add(aramaAlani, BorderLayout.CENTER);
        filtrePaneli.add(sadeceBekleyenler, BorderLayout.EAST);

        JPanel temaPaneli = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        temaPaneli.setOpaque(false);

        temaBtn = new JButton("🌙 Dark Mode");
        stilButon(temaBtn, new Color(80, 80, 80));
        temaPaneli.add(temaBtn);

        ustPanel.add(eklemePaneli);
        ustPanel.add(filtrePaneli);
        ustPanel.add(temaPaneli);

        String[] kolonlar = {"ID", "Görev", "Durum", "Tarih"};
        tableModel = new DefaultTableModel(kolonlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel altPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        altPanel.setOpaque(false);

        yenileBtn = new JButton("Listeyi Yenile");
        tamamlaBtn = new JButton("Seçiliyi Tamamla");
        silBtn = new JButton("Seçiliyi Sil");

        stilButon(yenileBtn, new Color(46, 204, 113));
        stilButon(tamamlaBtn, new Color(241, 196, 15));
        stilButon(silBtn, new Color(231, 76, 60));

        altPanel.add(yenileBtn);
        altPanel.add(tamamlaBtn);
        altPanel.add(silBtn);

        anaPanel.add(baslik, BorderLayout.NORTH);
        anaPanel.add(ustPanel, BorderLayout.BEFORE_FIRST_LINE);
        anaPanel.add(scrollPane, BorderLayout.CENTER);
        anaPanel.add(altPanel, BorderLayout.SOUTH);

        frame.setContentPane(anaPanel);

        temaUygula(); // başlangıç teması
        frame.setVisible(true);

        gorevleriYukle();

        ekleBtn.addActionListener(e -> {
            String gorev = gorevAlani.getText().trim();

            if (gorev.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Lütfen görev başlığı girin.");
                return;
            }

            GorevDAO.gorevEkle(gorev);
            gorevAlani.setText("");
            gorevleriYukle();
            JOptionPane.showMessageDialog(frame, "Görev eklendi.");
        });

        yenileBtn.addActionListener(e -> gorevleriYukle());

        tamamlaBtn.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Lütfen bir görev seçin.");
                return;
            }

            int id = (int) tableModel.getValueAt(row, 0);
            GorevDAO.gorevTamamla(id);
            gorevleriYukle();
            JOptionPane.showMessageDialog(frame, "Görev tamamlandı.");
        });

        silBtn.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Lütfen bir görev seçin.");
                return;
            }

            int cevap = JOptionPane.showConfirmDialog(
                    frame,
                    "Bu görevi silmek istiyor musunuz?",
                    "Silme Onayı",
                    JOptionPane.YES_NO_OPTION
            );

            if (cevap == JOptionPane.YES_OPTION) {
                int id = (int) tableModel.getValueAt(row, 0);
                GorevDAO.gorevSil(id);
                gorevleriYukle();
                JOptionPane.showMessageDialog(frame, "Görev silindi.");
            }
        });

        aramaAlani.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                gorevleriYukle();
            }
        });

        sadeceBekleyenler.addActionListener(e -> gorevleriYukle());

        temaBtn.addActionListener(e -> {
            darkMode = !darkMode;
            temaUygula();
        });
    }

    private void gorevleriYukle() {
        tableModel.setRowCount(0);

        ArrayList<Gorev> liste = GorevDAO.gorevleriGetir();
        String aranan = aramaAlani.getText().trim().toLowerCase();
        boolean sadeceBekleyen = sadeceBekleyenler.isSelected();

        for (Gorev g : liste) {
            boolean aramaUyuyor = g.getBaslik().toLowerCase().contains(aranan);
            boolean durumUyuyor = !sadeceBekleyen || !g.isTamamlandi();

            if (aramaUyuyor && durumUyuyor) {
                tableModel.addRow(new Object[]{
                        g.getId(),
                        g.getBaslik(),
                        g.isTamamlandi() ? "Tamamlandı" : "Bekliyor",
                        g.getTarih()
                });
            }
        }
    }

    private void temaUygula() {
        if (darkMode) {
            anaPanel.setBackground(new Color(30, 30, 30));
            baslik.setForeground(Color.WHITE);
            araLabel.setForeground(Color.WHITE);

            gorevAlani.setBackground(new Color(50, 50, 50));
            gorevAlani.setForeground(Color.WHITE);
            gorevAlani.setCaretColor(Color.WHITE);

            aramaAlani.setBackground(new Color(50, 50, 50));
            aramaAlani.setForeground(Color.WHITE);
            aramaAlani.setCaretColor(Color.WHITE);

            sadeceBekleyenler.setForeground(Color.WHITE);

            table.setBackground(new Color(45, 45, 45));
            table.setForeground(Color.WHITE);
            table.setSelectionBackground(new Color(70, 130, 180));
            table.setSelectionForeground(Color.WHITE);

            table.getTableHeader().setBackground(new Color(60, 60, 60));
            table.getTableHeader().setForeground(Color.WHITE);

            temaBtn.setText("☀️ Light Mode");

        } else {
            anaPanel.setBackground(new Color(245, 247, 250));
            baslik.setForeground(new Color(40, 40, 40));
            araLabel.setForeground(Color.BLACK);

            gorevAlani.setBackground(Color.WHITE);
            gorevAlani.setForeground(Color.BLACK);
            gorevAlani.setCaretColor(Color.BLACK);

            aramaAlani.setBackground(Color.WHITE);
            aramaAlani.setForeground(Color.BLACK);
            aramaAlani.setCaretColor(Color.BLACK);

            sadeceBekleyenler.setForeground(Color.BLACK);

            table.setBackground(Color.WHITE);
            table.setForeground(Color.BLACK);
            table.setSelectionBackground(new Color(52, 152, 219));
            table.setSelectionForeground(Color.WHITE);

            table.getTableHeader().setBackground(new Color(230, 230, 230));
            table.getTableHeader().setForeground(Color.BLACK);

            temaBtn.setText("🌙 Dark Mode");
        }
    }

    private static void stilButon(JButton buton, Color renk) {
        buton.setFocusPainted(false);
        buton.setBackground(renk);
        buton.setForeground(Color.WHITE);
        buton.setFont(new Font("Arial", Font.BOLD, 14));
        buton.setPreferredSize(new Dimension(160, 38));
    }
}