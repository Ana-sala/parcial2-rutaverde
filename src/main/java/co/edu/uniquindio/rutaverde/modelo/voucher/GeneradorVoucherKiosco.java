package co.edu.uniquindio.rutaverde.modelo.voucher;

public class GeneradorVoucherKiosco extends GeneradorVoucher {

    public GeneradorVoucherKiosco(String nombreAgencia, String rnt) {
        super(nombreAgencia, rnt);
    }

    @Override
    protected Voucher crearVoucher() { return new TiqueteKiosco(); }
}
