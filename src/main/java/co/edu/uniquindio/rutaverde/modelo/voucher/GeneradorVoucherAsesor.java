package co.edu.uniquindio.rutaverde.modelo.voucher;

public class GeneradorVoucherAsesor extends GeneradorVoucher {

    public GeneradorVoucherAsesor(String nombreAgencia, String rnt) {
        super(nombreAgencia, rnt);
    }

    @Override
    protected Voucher crearVoucher() { return new VoucherDetallado(); }
}
