
public class OItemDoor extends OItem {

    private OMaterial a;

    public OItemDoor(int i, OMaterial omaterial) {
        super(i);
        this.a = omaterial;
        this.bU = 1;
        this.a(OCreativeTabs.d);
    }

    public boolean a(OItemStack oitemstack, OEntityPlayer oentityplayer, OWorld oworld, int i, int j, int k, int l, float f, float f1, float f2) {
        if (l != 1) {
            return false;
        } else {
            ++j;
            OBlock oblock;

            if (this.a == OMaterial.d) {
                oblock = OBlock.aE;
            } else {
                oblock = OBlock.aL;
            }

            if (oentityplayer.e(i, j, k) && oentityplayer.e(i, j + 1, k)) {
                if (!oblock.b(oworld, i, j, k)) {
                    return false;
                } else {
                    // CanaryMod hook: onItemUse
                    Block blockClicked = this.getBlockInfo(oworld, i, j, k, l);
                    Block blockPlaced = new Block(oworld.world, oblock.ca, i, j, k);

                    // Call the hook
                    if (oentityplayer instanceof OEntityPlayerMP) {
                        Player player = ((OEntityPlayerMP) oentityplayer).getPlayer();

                        if ((Boolean) etc.getLoader().callHook(PluginLoader.Hook.ITEM_USE, player, blockPlaced, blockClicked, new Item(oitemstack))) {
                            return false;
                        }
                    }
                    
                    int i1 = OMathHelper.c((double) ((oentityplayer.z + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;

                    a(oworld, i, j, k, i1, oblock);
                    --oitemstack.a;
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public static void a(OWorld oworld, int i, int j, int k, int l, OBlock oblock) {
        byte b0 = 0;
        byte b1 = 0;

        if (l == 0) {
            b1 = 1;
        }

        if (l == 1) {
            b0 = -1;
        }

        if (l == 2) {
            b1 = -1;
        }

        if (l == 3) {
            b0 = 1;
        }

        int i1 = (oworld.s(i - b0, j, k - b1) ? 1 : 0) + (oworld.s(i - b0, j + 1, k - b1) ? 1 : 0);
        int j1 = (oworld.s(i + b0, j, k + b1) ? 1 : 0) + (oworld.s(i + b0, j + 1, k + b1) ? 1 : 0);
        boolean flag = oworld.a(i - b0, j, k - b1) == oblock.ca || oworld.a(i - b0, j + 1, k - b1) == oblock.ca;
        boolean flag1 = oworld.a(i + b0, j, k + b1) == oblock.ca || oworld.a(i + b0, j + 1, k + b1) == oblock.ca;
        boolean flag2 = false;

        if (flag && !flag1) {
            flag2 = true;
        } else if (j1 > i1) {
            flag2 = true;
        }

        oworld.t = true;
        oworld.d(i, j, k, oblock.ca, l);
        oworld.d(i, j + 1, k, oblock.ca, 8 | (flag2 ? 1 : 0));
        oworld.t = false;
        oworld.h(i, j, k, oblock.ca);
        oworld.h(i, j + 1, k, oblock.ca);
    }
}
