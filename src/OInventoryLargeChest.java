
public class OInventoryLargeChest implements OIInventory, Container<OItemStack> {

    private String a;
    private OIInventory b;
    private OIInventory c;

    public OInventoryLargeChest(String s, OIInventory oiinventory, OIInventory oiinventory1) {
        super();
        this.a = s;
        if (oiinventory == null) {
            oiinventory = oiinventory1;
        }

        if (oiinventory1 == null) {
            oiinventory1 = oiinventory;
        }

        this.b = oiinventory;
        this.c = oiinventory1;
    }

    public int i_() {
        return this.b.i_() + this.c.i_();
    }

    public String b() {
        return this.a;
    }

    public OItemStack a(int i) {
        return i >= this.b.i_() ? this.c.a(i - this.b.i_()) : this.b.a(i);
    }

    public OItemStack a(int i, int j) {
        return i >= this.b.i_() ? this.c.a(i - this.b.i_(), j) : this.b.a(i, j);
    }

    public OItemStack b(int i) {
        return i >= this.b.i_() ? this.c.b(i - this.b.i_()) : this.b.b(i);
    }

    public void a(int i, OItemStack oitemstack) {
        if (i >= this.b.i_()) {
            this.c.a(i - this.b.i_(), oitemstack);
        } else {
            this.b.a(i, oitemstack);
        }

    }

    public int j_() {
        return this.b.j_();
    }

    public void d() {
        this.b.d();
        this.c.d();
    }

    public boolean a(OEntityPlayer oentityplayer) {
        return this.b.a(oentityplayer) && this.c.a(oentityplayer);
    }

    public void k_() {
        this.b.k_();
        this.c.k_();
    }

    public void f() {
        this.b.f();
        this.c.f();
    }
    
    public OItemStack[] getContents() {
        int size = this.getContentsSize();
        OItemStack[] result = new OItemStack[size];

        for (int i = 0; i < size; i++) {
            result[i] = getContentsAt(i);
        }
        return result;
    }

    public void setContents(OItemStack[] aoitemstack) {
        int size = this.getContentsSize();

        for (int i = 0; i < size; i++) {
            this.setContentsAt(i, aoitemstack[i]);
        }
    }

    public OItemStack getContentsAt(int i) {
        return this.a(i);
    }

    public void setContentsAt(int i, OItemStack oitemstack) {
        this.a(i, oitemstack);
    }

    public int getContentsSize() {
        return this.i_();
    }

    public String getName() {
        return this.a;
    }

    public void setName(String s) {
        this.a = s;
    }

    public Block getChestBlock() {
        if (this.b instanceof OTileEntityChest) {
            OTileEntityChest block = (OTileEntityChest) this.b;

            return block.k.world.getBlockAt(block.l, block.m, block.n);
        }
        if (this.c instanceof OTileEntityChest) {
            OTileEntityChest block = (OTileEntityChest) this.c;

            return block.k.world.getBlockAt(block.l, block.m, block.n);
        }
        return null;
    }
}
