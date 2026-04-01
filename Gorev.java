public class Gorev {
    private int id;
    private String baslik;
    private boolean tamamlandi;
    private String tarih;

    public Gorev(int id, String baslik, boolean tamamlandi, String tarih) {
        this.id = id;
        this.baslik = baslik;
        this.tamamlandi = tamamlandi;
        this.tarih = tarih;
    }

    public int getId() {
        return id;
    }

    public String getBaslik() {
        return baslik;
    }

    public boolean isTamamlandi() {
        return tamamlandi;
    }

    public String getTarih() {
        return tarih;
    }
}