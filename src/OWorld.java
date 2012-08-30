import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

public abstract class OWorld implements OIBlockAccess {
    
    public boolean e = false;
    public List f = new ArrayList();
    protected List g = new ArrayList();
    public List h = new ArrayList();
    private List a = new ArrayList();
    private List b = new ArrayList();
    public List i = new ArrayList();
    public List j = new ArrayList();
    private long c = 16777215L;
    public int k = 0;
    protected int l = (new Random()).nextInt();
    protected final int m = 1013904223;
    protected float n;
    protected float o;
    protected float p;
    protected float q;
    protected int r = 0;
    public int s = 0;
    public boolean t = false;
    public int u;
    public Random v = new Random();
    public final OWorldProvider w;
    protected List x = new ArrayList();
    protected OIChunkProvider y;
    protected final OISaveHandler z;
    protected OWorldInfo A;
    public boolean B;
    public OMapStorage C;
    public final OVillageCollection D = new OVillageCollection(this);
    protected final OVillageSiege E = new OVillageSiege(this);
    public final OProfiler F;
    private ArrayList d = new ArrayList();
    private boolean L;
    protected boolean G = true;
    protected boolean H = true;
    protected Set I = new HashSet();
    private int M;
    int[] J;
    private List N;
    public boolean K;
    
    // CanaryMod
    public final World world = new World((OWorldServer) this);
    boolean loadedpreload = false;
    public final String name;

    public OBiomeGenBase a(int i, int j) {
        if (this.e(i, 0, j)) {
            OChunk ochunk = this.d(i, j);

            if (ochunk != null) {
                return ochunk.a(i & 15, j & 15, this.w.c);
            }
        }

        return this.w.c.a(i, j);
    }

    public OWorldChunkManager r() {
        return this.w.c;
    }

    public OWorld(OISaveHandler oisavehandler, String s, OWorldSettings oworldsettings, OWorldProvider oworldprovider, OProfiler oprofiler) {
        this.M = this.v.nextInt(12000);
        this.J = new int['\u8000'];
        this.N = new ArrayList();
        this.K = false;
        this.z = oisavehandler;
        this.F = oprofiler;
        this.C = new OMapStorage(oisavehandler);
        this.A = oisavehandler.d();
        if (oworldprovider != null) {
            this.w = oworldprovider;
        } else if (this.A != null && this.A.i() != 0) {
            this.w = OWorldProvider.a(this.A.i());
        } else {
            this.w = OWorldProvider.a(0);
        }

        if (this.A == null) {
            this.A = new OWorldInfo(oworldsettings, s);
        } else {
            this.A.a(s);
        }

        this.w.a(this);
        this.y = this.i();
        if (!this.A.v()) {
            this.a(oworldsettings);
            this.A.d(true);
        }

        this.v();
        this.a();
        
        this.name = s; // CanaryMod: store world name in an accessible place.
    }

    protected abstract OIChunkProvider i();

    protected void a(OWorldSettings oworldsettings) {
        this.A.d(true);
    }

    public int b(int i, int j) {
        int k;

        for (k = 63; !this.c(i, k + 1, j); ++k) {
            ;
        }

        return this.a(i, k, j);
    }

    public int a(int i, int j, int k) {
        return i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000 ? (j < 0 ? 0 : (j >= 256 ? 0 : this.e(i >> 4, k >> 4).a(i & 15, j, k & 15))) : 0;
    }

    public int b(int i, int j, int k) {
        return i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000 ? (j < 0 ? 0 : (j >= 256 ? 0 : this.e(i >> 4, k >> 4).b(i & 15, j, k & 15))) : 0;
    }

    public boolean c(int i, int j, int k) {
        return this.a(i, j, k) == 0;
    }

    public boolean d(int i, int j, int k) {
        int l = this.a(i, j, k);

        return OBlock.m[l] != null && OBlock.m[l].s();
    }

    public boolean e(int i, int j, int k) {
        return j >= 0 && j < 256 ? this.c(i >> 4, k >> 4) : false;
    }

    public boolean a(int i, int j, int k, int l) {
        return this.c(i - l, j - l, k - l, i + l, j + l, k + l);
    }

    public boolean c(int i, int j, int k, int l, int i1, int j1) {
        if (i1 >= 0 && j < 256) {
            i >>= 4;
            k >>= 4;
            l >>= 4;
            j1 >>= 4;

            for (int k1 = i; k1 <= l; ++k1) {
                for (int l1 = k; l1 <= j1; ++l1) {
                    if (!this.c(k1, l1)) {
                        return false;
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected boolean c(int i, int j) {
        return (this.y != null ? this.y.a(i, j) : false); // CanaryMod: fix NPE
    }

    public OChunk d(int i, int j) {
        return this.e(i >> 4, j >> 4);
    }

    public OChunk e(int i, int j) {
        return this.y.d(i, j);
    }

    public boolean c(int i, int j, int k, int l, int i1) {
        return this.a(i, j, k, l, i1, true);
    }

    public boolean a(int i, int j, int k, int l, int i1, boolean flag) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return false;
            } else if (j >= 256) {
                return false;
            } else {
                OChunk ochunk = this.e(i >> 4, k >> 4);
                boolean flag1 = false;
                // CanaryMod: Get Block Info
                Block block = this.world.getBlockAt(i, j, k);
                // CanaryMod ignore if new block is air
                if (l == 0 || !(Boolean) OEntity.manager.callHook(PluginLoader.Hook.BLOCK_UPDATE, block, l)) {
                    flag1 = ochunk.a(i & 15, j, k & 15, l, i1);
                }
                this.F.a("checkLight");
                this.x(i, j, k);
                this.F.b();
                if (flag && flag1 && (this.K || ochunk.o)) {
                    this.h(i, j, k);
                }

                return flag1;
            }
        } else {
            return false;
        }
    }

    public boolean b(int i, int j, int k, int l) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return false;
            } else if (j >= 256) {
                return false;
            } else {
                OChunk ochunk = this.e(i >> 4, k >> 4);
                // CanaryMod Block Updates
                // Get Block Info
                Block block = this.world.getBlockAt(i, j, k);
                // ignore if new block is air
                boolean flag = false;

                if (l == 0 || !(Boolean) OEntity.manager.callHook(PluginLoader.Hook.BLOCK_UPDATE, block, l)) {
                    flag = ochunk.a(i & 15, j, k & 15, l);
                }
                this.F.a("checkLight");
                this.x(i, j, k);
                this.F.b();
                if (flag && (this.K || ochunk.o)) {
                    this.h(i, j, k);
                }

                return flag;
            }
        } else {
            return false;
        }
    }

    public OMaterial f(int i, int j, int k) {
        int l = this.a(i, j, k);

        return l == 0 ? OMaterial.a : OBlock.m[l].cp;
    }

    public int g(int i, int j, int k) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return 0;
            } else if (j >= 256) {
                return 0;
            } else {
                OChunk ochunk = this.e(i >> 4, k >> 4);

                i &= 15;
                k &= 15;
                return ochunk.c(i, j, k);
            }
        } else {
            return 0;
        }
    }

    public void c(int i, int j, int k, int l) {
        if (this.d(i, j, k, l)) {
            this.f(i, j, k, this.a(i, j, k));
        }
    }

    public boolean d(int i, int j, int k, int l) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j < 0) {
                return false;
            } else if (j >= 256) {
                return false;
            } else {
                OChunk ochunk = this.e(i >> 4, k >> 4);
                int i1 = i & 15;
                int j1 = k & 15;
                boolean flag = ochunk.b(i1, j, j1, l);

                if (flag && (this.K || ochunk.o && OBlock.r[ochunk.a(i1, j, j1) & 4095])) {
                    this.h(i, j, k);
                }

                return flag;
            }
        } else {
            return false;
        }
    }

    public boolean e(int i, int j, int k, int l) {
        if (this.b(i, j, k, l)) {
            this.f(i, j, k, l);
            return true;
        } else {
            return false;
        }
    }

    public boolean d(int i, int j, int k, int l, int i1) {
        if (this.c(i, j, k, l, i1)) {
            this.f(i, j, k, l);
            return true;
        } else {
            return false;
        }
    }

    public void h(int i, int j, int k) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            OIWorldAccess oiworldaccess = (OIWorldAccess) iterator.next();

            oiworldaccess.a(i, j, k);
        }
    }

    public void f(int i, int j, int k, int l) {
        this.h(i, j, k, l);
    }

    public void g(int i, int j, int k, int l) {
        int i1;

        if (k > l) {
            i1 = l;
            l = k;
            k = i1;
        }

        if (!this.w.e) {
            for (i1 = k; i1 <= l; ++i1) {
                this.c(OEnumSkyBlock.a, i, i1, j);
            }
        }

        this.d(i, k, j, i, l, j);
    }

    public void i(int i, int j, int k) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            OIWorldAccess oiworldaccess = (OIWorldAccess) iterator.next();

            oiworldaccess.a(i, j, k, i, j, k);
        }
    }

    public void d(int i, int j, int k, int l, int i1, int j1) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            OIWorldAccess oiworldaccess = (OIWorldAccess) iterator.next();

            oiworldaccess.a(i, j, k, l, i1, j1);
       }
    }

    public void h(int i, int j, int k, int l) {
        this.m(i - 1, j, k, l);
        this.m(i + 1, j, k, l);
        this.m(i, j - 1, k, l);
        this.m(i, j + 1, k, l);
        this.m(i, j, k - 1, l);
        this.m(i, j, k + 1, l);
    }

    private void m(int i, int j, int k, int l) {
        if (!this.t && !this.K) {
            OBlock oblock = OBlock.m[this.a(i, j, k)];

            if (oblock != null) {
                oblock.a(this, i, j, k, l);
            }
        }
    }

    public boolean j(int i, int j, int k) {
        return this.e(i >> 4, k >> 4).d(i & 15, j, k & 15);
    }

    public int k(int i, int j, int k) {
        if (j < 0) {
            return 0;
        } else {
            if (j >= 256) {
                j = 255;
            }

            return this.e(i >> 4, k >> 4).c(i & 15, j, k & 15, 0);
        }
    }

    public int l(int i, int j, int k) {
        return this.a(i, j, k, true);
    }

    public int a(int i, int j, int k, boolean flag) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (flag) {
                int l = this.a(i, j, k);

                if (l == OBlock.ak.ca || l == OBlock.bO.ca || l == OBlock.aA.ca || l == OBlock.aH.ca || l == OBlock.at.ca) {
                    int i1 = this.a(i, j + 1, k, false);
                    int j1 = this.a(i + 1, j, k, false);
                    int k1 = this.a(i - 1, j, k, false);
                    int l1 = this.a(i, j, k + 1, false);
                    int i2 = this.a(i, j, k - 1, false);

                    if (j1 > i1) {
                        i1 = j1;
                    }

                    if (k1 > i1) {
                        i1 = k1;
                    }

                    if (l1 > i1) {
                        i1 = l1;
                    }

                    if (i2 > i1) {
                        i1 = i2;
                    }

                    return i1;
                }
            }

            if (j < 0) {
                return 0;
            } else {
                if (j >= 256) {
                    j = 255;
                }

                OChunk ochunk = this.e(i >> 4, k >> 4);

                i &= 15;
                k &= 15;
                return ochunk.c(i, j, k, this.k);
            }
        } else {
            return 15;
        }
    }

    public int f(int i, int j) {
        if (i >= -30000000 && j >= -30000000 && i < 30000000 && j < 30000000) {
            if (!this.c(i >> 4, j >> 4)) {
                return 0;
            } else {
                OChunk ochunk = this.e(i >> 4, j >> 4);

                return ochunk.b(i & 15, j & 15);
            }
        } else {
            return 0;
        }
    }

    public int b(OEnumSkyBlock oenumskyblock, int i, int j, int k) {
        if (j < 0) {
            j = 0;
        }

        if (j >= 256) {
            j = 255;
        }

        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            int l = i >> 4;
            int i1 = k >> 4;

            if (!this.c(l, i1)) {
                return oenumskyblock.c;
            } else {
                OChunk ochunk = this.e(l, i1);

                return ochunk.a(oenumskyblock, i & 15, j, k & 15);
            }
        } else {
            return oenumskyblock.c;
        }
    }

    public void b(OEnumSkyBlock oenumskyblock, int i, int j, int k, int l) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            if (j >= 0) {
                if (j < 256) {
                    if (this.c(i >> 4, k >> 4)) {
                        OChunk ochunk = this.e(i >> 4, k >> 4);

                        ochunk.a(oenumskyblock, i & 15, j, k & 15, l);
                        etc.getLoader().callHook(PluginLoader.Hook.LIGHT_CHANGE, i, j, k, l);
                        Iterator iterator = this.x.iterator();

                        while (iterator.hasNext()) {
                            OIWorldAccess oiworldaccess = (OIWorldAccess) iterator.next();

                            oiworldaccess.b(i, j, k);
                        }
                    }
                }
            }
        }
    }

    public void n(int i, int j, int k) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            OIWorldAccess oiworldaccess = (OIWorldAccess) iterator.next();

            oiworldaccess.b(i, j, k);
        }
    }

    public float o(int i, int j, int k) {
        return this.w.f[this.l(i, j, k)];
    }

    public boolean s() {
        return this.k < 4;
    }

    public OMovingObjectPosition a(OVec3 ovec3, OVec3 ovec31) {
        return this.a(ovec3, ovec31, false, false);
    }

    public OMovingObjectPosition a(OVec3 ovec3, OVec3 ovec31, boolean flag) {
        return this.a(ovec3, ovec31, flag, false);
    }

    public OMovingObjectPosition a(OVec3 ovec3, OVec3 ovec31, boolean flag, boolean flag1) {
        if (!Double.isNaN(ovec3.a) && !Double.isNaN(ovec3.b) && !Double.isNaN(ovec3.c)) {
            if (!Double.isNaN(ovec31.a) && !Double.isNaN(ovec31.b) && !Double.isNaN(ovec31.c)) {
                int i = OMathHelper.c(ovec31.a);
                int j = OMathHelper.c(ovec31.b);
                int k = OMathHelper.c(ovec31.c);
                int l = OMathHelper.c(ovec3.a);
                int i1 = OMathHelper.c(ovec3.b);
                int j1 = OMathHelper.c(ovec3.c);
                int k1 = this.a(l, i1, j1);
                int l1 = this.g(l, i1, j1);
                OBlock oblock = OBlock.m[k1];

                if ((!flag1 || oblock == null || oblock.e(this, l, i1, j1) != null) && k1 > 0 && oblock.a(l1, flag)) {
                    OMovingObjectPosition omovingobjectposition = oblock.a(this, l, i1, j1, ovec3, ovec31);

                    if (omovingobjectposition != null) {
                        return omovingobjectposition;
                    }
                }

                k1 = 200;

                while (k1-- >= 0) {
                    if (Double.isNaN(ovec3.a) || Double.isNaN(ovec3.b) || Double.isNaN(ovec3.c)) {
                        return null;
                    }

                    if (l == i && i1 == j && j1 == k) {
                        return null;
                    }

                    boolean flag2 = true;
                    boolean flag3 = true;
                    boolean flag4 = true;
                    double d0 = 999.0D;
                    double d1 = 999.0D;
                    double d2 = 999.0D;

                    if (i > l) {
                        d0 = (double) l + 1.0D;
                    } else if (i < l) {
                        d0 = (double) l + 0.0D;
                    } else {
                        flag2 = false;
                    }

                    if (j > i1) {
                        d1 = (double) i1 + 1.0D;
                    } else if (j < i1) {
                        d1 = (double) i1 + 0.0D;
                    } else {
                        flag3 = false;
                    }

                    if (k > j1) {
                        d2 = (double) j1 + 1.0D;
                    } else if (k < j1) {
                        d2 = (double) j1 + 0.0D;
                    } else {
                        flag4 = false;
                    }

                    double d3 = 999.0D;
                    double d4 = 999.0D;
                    double d5 = 999.0D;
                    double d6 = ovec31.a - ovec3.a;
                    double d7 = ovec31.b - ovec3.b;
                    double d8 = ovec31.c - ovec3.c;

                    if (flag2) {
                        d3 = (d0 - ovec3.a) / d6;
                    }

                    if (flag3) {
                        d4 = (d1 - ovec3.b) / d7;
                    }

                    if (flag4) {
                        d5 = (d2 - ovec3.c) / d8;
                    }

                    boolean flag5 = false;
                    byte b0;

                    if (d3 < d4 && d3 < d5) {
                        if (i > l) {
                            b0 = 4;
                        } else {
                            b0 = 5;
                        }

                        ovec3.a = d0;
                        ovec3.b += d7 * d3;
                        ovec3.c += d8 * d3;
                    } else if (d4 < d5) {
                        if (j > i1) {
                            b0 = 0;
                        } else {
                            b0 = 1;
                        }

                        ovec3.a += d6 * d4;
                        ovec3.b = d1;
                        ovec3.c += d8 * d4;
                    } else {
                        if (k > j1) {
                            b0 = 2;
                        } else {
                            b0 = 3;
                        }

                        ovec3.a += d6 * d5;
                        ovec3.b += d7 * d5;
                        ovec3.c = d2;
                    }

                    OVec3 ovec32 = OVec3.a().a(ovec3.a, ovec3.b, ovec3.c);

                    l = (int) (ovec32.a = (double) OMathHelper.c(ovec3.a));
                    if (b0 == 5) {
                        --l;
                        ++ovec32.a;
                    }

                    i1 = (int) (ovec32.b = (double) OMathHelper.c(ovec3.b));
                    if (b0 == 1) {
                        --i1;
                        ++ovec32.b;
                    }

                    j1 = (int) (ovec32.c = (double) OMathHelper.c(ovec3.c));
                    if (b0 == 3) {
                        --j1;
                        ++ovec32.c;
                    }

                    int i2 = this.a(l, i1, j1);
                    int j2 = this.g(l, i1, j1);
                    OBlock oblock1 = OBlock.m[i2];

                    if ((!flag1 || oblock1 == null || oblock1.e(this, l, i1, j1) != null) && i2 > 0 && oblock1.a(j2, flag)) {
                        OMovingObjectPosition omovingobjectposition1 = oblock1.a(this, l, i1, j1, ovec3, ovec31);

                        if (omovingobjectposition1 != null) {
                            return omovingobjectposition1;
                        }
                    }
                }

                return null;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void a(OEntity oentity, String s, float f, float f1) {
        if (oentity != null && s != null) {
            Iterator iterator = this.x.iterator();

            while (iterator.hasNext()) {
                OIWorldAccess oiworldaccess = (OIWorldAccess) iterator.next();

                oiworldaccess.a(s, oentity.t, oentity.u - (double) oentity.M, oentity.v, f, f1);
            }
        }
    }

    public void a(double d0, double d1, double d2, String s, float f, float f1) {
        if (s != null) {
            Iterator iterator = this.x.iterator();

            while (iterator.hasNext()) {
                OIWorldAccess oiworldaccess = (OIWorldAccess) iterator.next();

                oiworldaccess.a(s, d0, d1, d2, f, f1);
            }
        }
    }

    public void a(String s, int i, int j, int k) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            OIWorldAccess oiworldaccess = (OIWorldAccess) iterator.next();

            oiworldaccess.a(s, i, j, k);
        }
    }

    public void a(String s, double d0, double d1, double d2, double d3, double d4, double d5) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            OIWorldAccess oiworldaccess = (OIWorldAccess) iterator.next();

            oiworldaccess.a(s, d0, d1, d2, d3, d4, d5);
        }
    }

    public boolean c(OEntity oentity) {
        this.j.add(oentity);
        return true;
    }

    public boolean d(OEntity oentity) {
        // CanaryMod: mob spawn hook
        if (oentity instanceof OEntityLiving && !(oentity instanceof OEntityPlayer)) {
            if ((etc.getInstance().getMobSpawnRate() < 100 && etc.getInstance().getMobSpawnRate() > 0 && etc.getInstance().getMobSpawnRate() <= this.v.nextInt(101)) || etc.getInstance().getMobSpawnRate() <= 0 || (Boolean) (etc.getLoader().callHook(PluginLoader.Hook.MOB_SPAWN, new Mob((OEntityLiving) oentity)))) {
                return false;
            }
        }
        
        int i = OMathHelper.c(oentity.t / 16.0D);
        int j = OMathHelper.c(oentity.v / 16.0D);
        boolean flag = false;

        if (oentity instanceof OEntityPlayer) {
            flag = true;
        }

        if (!flag && !this.c(i, j)) {
            return false;
        } else {
            if (oentity instanceof OEntityPlayer) {
                OEntityPlayer oentityplayer = (OEntityPlayer) oentity;

                this.i.add(oentityplayer);
                this.c();
            }

            this.e(i, j).a(oentity);
            this.f.add(oentity);
            this.a(oentity);
            return true;
        }
    }

    protected void a(OEntity oentity) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            OIWorldAccess oiworldaccess = (OIWorldAccess) iterator.next();

            oiworldaccess.a(oentity);
        }
    }

    protected void b(OEntity oentity) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            OIWorldAccess oiworldaccess = (OIWorldAccess) iterator.next();

            oiworldaccess.b(oentity);
        }
    }

    public void e(OEntity oentity) {
        if (oentity.n != null) {
            oentity.n.a((OEntity) null);
        }

        if (oentity.o != null) {
            oentity.a((OEntity) null);
        }

        oentity.y();
        if (oentity instanceof OEntityPlayer) {
            this.i.remove(oentity);
            this.c();
        }
    }

    public void f(OEntity oentity) {
        oentity.y();
        if (oentity instanceof OEntityPlayer) {
            this.i.remove(oentity);
            this.c();
        }

        int i = oentity.ah;
        int j = oentity.aj;

        if (oentity.ag && this.c(i, j)) {
            this.e(i, j).b(oentity);
        }

        this.f.remove(oentity);
        this.b(oentity);
    }

    public void a(OIWorldAccess oiworldaccess) {
        this.x.add(oiworldaccess);
    }

    public List a(OEntity oentity, OAxisAlignedBB oaxisalignedbb) {
        this.d.clear();
        int i = OMathHelper.c(oaxisalignedbb.a);
        int j = OMathHelper.c(oaxisalignedbb.d + 1.0D);
        int k = OMathHelper.c(oaxisalignedbb.b);
        int l = OMathHelper.c(oaxisalignedbb.e + 1.0D);
        int i1 = OMathHelper.c(oaxisalignedbb.c);
        int j1 = OMathHelper.c(oaxisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = i1; l1 < j1; ++l1) {
                if (this.e(k1, 64, l1)) {
                    for (int i2 = k - 1; i2 < l; ++i2) {
                        OBlock oblock = OBlock.m[this.a(k1, i2, l1)];

                        if (oblock != null) {
                            oblock.a(this, k1, i2, l1, oaxisalignedbb, this.d, oentity);
                        }
                    }
                }
            }
        }

        // CanaryMod: Implemented fix via M4411K4 VEHICLE_COLLISION hook
        BaseVehicle vehicle = null;

        if (oentity instanceof OEntityMinecart) {
            vehicle = ((OEntityMinecart) oentity).cart;
        } else if (oentity instanceof OEntityBoat) {
            vehicle = ((OEntityBoat) oentity).boat;
        }

        double d0 = 0.25D;
        List list = this.b(oentity, oaxisalignedbb.b(d0, d0, d0));
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            OEntity oentity1 = (OEntity) iterator.next();
            OAxisAlignedBB oaxisalignedbb1 = oentity1.E();
            
            if (oaxisalignedbb1 != null && oaxisalignedbb1.a(oaxisalignedbb)) {
                // CanaryMod: this collided with a boat
                if (vehicle != null && (Boolean) etc.getLoader().callHook(PluginLoader.Hook.VEHICLE_COLLISION, vehicle, oentity1.entity)) {
                    continue;
                }
                
                this.d.add(oaxisalignedbb1);
            }

            oaxisalignedbb1 = oentity.g(oentity1);
            if (oaxisalignedbb1 != null && oaxisalignedbb1.a(oaxisalignedbb)) {
                // CanaryMod: this collided with entity
                if (vehicle != null && (Boolean) etc.getLoader().callHook(PluginLoader.Hook.VEHICLE_COLLISION, vehicle, oentity1.entity)) {
                    continue;
                }
                
                this.d.add(oaxisalignedbb1);
            }
        }

        return this.d;
    }

    public List a(OAxisAlignedBB oaxisalignedbb) {
        this.d.clear();
        int i = OMathHelper.c(oaxisalignedbb.a);
        int j = OMathHelper.c(oaxisalignedbb.d + 1.0D);
        int k = OMathHelper.c(oaxisalignedbb.b);
        int l = OMathHelper.c(oaxisalignedbb.e + 1.0D);
        int i1 = OMathHelper.c(oaxisalignedbb.c);
        int j1 = OMathHelper.c(oaxisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = i1; l1 < j1; ++l1) {
                if (this.e(k1, 64, l1)) {
                    for (int i2 = k - 1; i2 < l; ++i2) {
                        OBlock oblock = OBlock.m[this.a(k1, i2, l1)];

                        if (oblock != null) {
                            oblock.a(this, k1, i2, l1, oaxisalignedbb, this.d, (OEntity) null);
                        }
                    }
                }
            }
        }
        
        return this.d;
    }

    public int a(float f) {
        float f1 = this.c(f);
        float f2 = 1.0F - (OMathHelper.b(f1 * 3.1415927F * 2.0F) * 2.0F + 0.5F);

        if (f2 < 0.0F) {
            f2 = 0.0F;
        }

        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        f2 = 1.0F - f2;
        f2 = (float) ((double) f2 * (1.0D - (double) (this.j(f) * 5.0F) / 16.0D));
        f2 = (float) ((double) f2 * (1.0D - (double) (this.i(f) * 5.0F) / 16.0D));
        f2 = 1.0F - f2;
        return (int) (f2 * 11.0F);
    }

    public float c(float f) {
        return this.w.a(this.A.f(), f);
    }

    public int g(int i, int j) {
        return this.d(i, j).d(i & 15, j & 15);
    }

    public int h(int i, int j) {
        OChunk ochunk = this.d(i, j);
        int k = ochunk.h() + 15;

        i &= 15;

        for (j &= 15; k > 0; --k) {
            int l = ochunk.a(i, k, j);

            if (l != 0 && OBlock.m[l].cp.c() && OBlock.m[l].cp != OMaterial.i) {
                return k + 1;
            }
        }

        return -1;
    }

    public void a(int i, int j, int k, int l, int i1) {}

    public void b(int i, int j, int k, int l, int i1) {}

    public void h() {
        this.F.a("entities");
        this.F.a("global");

        int i;
        OEntity oentity;

        for (i = 0; i < this.j.size(); ++i) {
            oentity = (OEntity) this.j.get(i);
            oentity.h_();
            if (oentity.L) {
                this.j.remove(i--);
            }
        }

        this.F.c("remove");
        this.f.removeAll(this.g);
        Iterator iterator = this.g.iterator();

        int j;
        int k;

        while (iterator.hasNext()) {
            oentity = (OEntity) iterator.next();
            j = oentity.ah;
            k = oentity.aj;
            if (oentity.ag && this.c(j, k)) {
                this.e(j, k).b(oentity);
            }
        }

        iterator = this.g.iterator();

        while (iterator.hasNext()) {
            oentity = (OEntity) iterator.next();
            this.b(oentity);
        }

        this.g.clear();
        this.F.c("regular");

        for (i = 0; i < this.f.size(); ++i) {
            oentity = (OEntity) this.f.get(i);
            if (oentity.o != null) {
                if (!oentity.o.L && oentity.o.n == oentity) {
                    continue;
                }

                oentity.o.n = null;
                oentity.o = null;
            }

            this.F.a("tick");
            if (!oentity.L) {
                this.g(oentity);
            }

            this.F.b();
            this.F.a("remove");
            if (oentity.L) {
                j = oentity.ah;
                k = oentity.aj;
                if (oentity.ag && this.c(j, k)) {
                    this.e(j, k).b(oentity);
                }

                this.f.remove(i--);
                this.b(oentity);
            }

            this.F.b();
        }

        this.F.c("tileEntities");
        this.L = true;
        iterator = this.h.iterator();

        while (iterator.hasNext()) {
            OTileEntity otileentity = (OTileEntity) iterator.next();

            if (!otileentity.p() && otileentity.m() && this.e(otileentity.l, otileentity.m, otileentity.n)) {
                otileentity.g();
            }

            if (otileentity.p()) {
                iterator.remove();
                if (this.c(otileentity.l >> 4, otileentity.n >> 4)) {
                    OChunk ochunk = this.e(otileentity.l >> 4, otileentity.n >> 4);

                    if (ochunk != null) {
                        ochunk.f(otileentity.l & 15, otileentity.m, otileentity.n & 15);
                    }
                }
            }
        }

        this.L = false;
        if (!this.b.isEmpty()) {
            this.h.removeAll(this.b);
            this.b.clear();
        }

        this.F.c("pendingTileEntities");
        if (!this.a.isEmpty()) {
            Iterator iterator1 = this.a.iterator();

            while (iterator1.hasNext()) {
                OTileEntity otileentity1 = (OTileEntity) iterator1.next();

                if (!otileentity1.p()) {
                    if (!this.h.contains(otileentity1)) {
                        this.h.add(otileentity1);
                    }

                    if (this.c(otileentity1.l >> 4, otileentity1.n >> 4)) {
                        OChunk ochunk1 = this.e(otileentity1.l >> 4, otileentity1.n >> 4);

                        if (ochunk1 != null) {
                            ochunk1.a(otileentity1.l & 15, otileentity1.m, otileentity1.n & 15, otileentity1);
                        }
                    }

                    this.h(otileentity1.l, otileentity1.m, otileentity1.n);
                }
            }

            this.a.clear();
        }

        this.F.b();
        this.F.b();
    }

    public void a(Collection collection) {
        if (this.L) {
            this.a.addAll(collection);
        } else {
            this.h.addAll(collection);
        }
    }

    public void g(OEntity oentity) {
        this.a(oentity, true);
    }

    public void a(OEntity oentity, boolean flag) {
        int i = OMathHelper.c(oentity.t);
        int j = OMathHelper.c(oentity.v);
        byte b0 = 32;

        if (!flag || this.c(i - b0, 0, j - b0, i + b0, 0, j + b0)) {
            oentity.S = oentity.t;
            oentity.T = oentity.u;
            oentity.U = oentity.v;
            oentity.B = oentity.z;
            oentity.C = oentity.A;
            if (flag && oentity.ag) {
                if (oentity.o != null) {
                    oentity.U();
                } else {
                    oentity.h_();
                }
            }

            this.F.a("chunkCheck");
            if (Double.isNaN(oentity.t) || Double.isInfinite(oentity.t)) {
                oentity.t = oentity.S;
            }

            if (Double.isNaN(oentity.u) || Double.isInfinite(oentity.u)) {
                oentity.u = oentity.T;
            }

            if (Double.isNaN(oentity.v) || Double.isInfinite(oentity.v)) {
                oentity.v = oentity.U;
            }

            if (Double.isNaN((double) oentity.A) || Double.isInfinite((double) oentity.A)) {
                oentity.A = oentity.C;
            }

            if (Double.isNaN((double) oentity.z) || Double.isInfinite((double) oentity.z)) {
                oentity.z = oentity.B;
            }

            int k = OMathHelper.c(oentity.t / 16.0D);
            int l = OMathHelper.c(oentity.u / 16.0D);
            int i1 = OMathHelper.c(oentity.v / 16.0D);

            if (!oentity.ag || oentity.ah != k || oentity.ai != l || oentity.aj != i1) {
                if (oentity.ag && this.c(oentity.ah, oentity.aj)) {
                    this.e(oentity.ah, oentity.aj).a(oentity, oentity.ai);
                }

                if (this.c(k, i1)) {
                    oentity.ag = true;
                    this.e(k, i1).a(oentity);
                } else {
                    oentity.ag = false;
                }
            }

            this.F.b();
            if (flag && oentity.ag && oentity.n != null) {
                if (!oentity.n.L && oentity.n.o == oentity) {
                    this.g(oentity.n);
                } else {
                    oentity.n.o = null;
                    oentity.n = null;
                }
            }
        }
    }

    public boolean b(OAxisAlignedBB oaxisalignedbb) {
        return this.a(oaxisalignedbb, (OEntity) null);
    }

    public boolean a(OAxisAlignedBB oaxisalignedbb, OEntity oentity) {
        List list = this.b((OEntity) null, oaxisalignedbb);
        Iterator iterator = list.iterator();

        OEntity oentity1;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            oentity1 = (OEntity) iterator.next();
        } while (oentity1.L || !oentity1.m || oentity1 == oentity);

        return false;
    }

    public boolean c(OAxisAlignedBB oaxisalignedbb) {
        int i = OMathHelper.c(oaxisalignedbb.a);
        int j = OMathHelper.c(oaxisalignedbb.d + 1.0D);
        int k = OMathHelper.c(oaxisalignedbb.b);
        int l = OMathHelper.c(oaxisalignedbb.e + 1.0D);
        int i1 = OMathHelper.c(oaxisalignedbb.c);
        int j1 = OMathHelper.c(oaxisalignedbb.f + 1.0D);

        if (oaxisalignedbb.a < 0.0D) {
            --i;
        }

        if (oaxisalignedbb.b < 0.0D) {
            --k;
        }

        if (oaxisalignedbb.c < 0.0D) {
            --i1;
        }

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    OBlock oblock = OBlock.m[this.a(k1, l1, i2)];

                    if (oblock != null) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean d(OAxisAlignedBB oaxisalignedbb) {
        int i = OMathHelper.c(oaxisalignedbb.a);
        int j = OMathHelper.c(oaxisalignedbb.d + 1.0D);
        int k = OMathHelper.c(oaxisalignedbb.b);
        int l = OMathHelper.c(oaxisalignedbb.e + 1.0D);
        int i1 = OMathHelper.c(oaxisalignedbb.c);
        int j1 = OMathHelper.c(oaxisalignedbb.f + 1.0D);

        if (oaxisalignedbb.a < 0.0D) {
            --i;
        }

        if (oaxisalignedbb.b < 0.0D) {
            --k;
        }

        if (oaxisalignedbb.c < 0.0D) {
            --i1;
        }

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    OBlock oblock = OBlock.m[this.a(k1, l1, i2)];

                    if (oblock != null && oblock.cp.d()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean e(OAxisAlignedBB oaxisalignedbb) {
        int i = OMathHelper.c(oaxisalignedbb.a);
        int j = OMathHelper.c(oaxisalignedbb.d + 1.0D);
        int k = OMathHelper.c(oaxisalignedbb.b);
        int l = OMathHelper.c(oaxisalignedbb.e + 1.0D);
        int i1 = OMathHelper.c(oaxisalignedbb.c);
        int j1 = OMathHelper.c(oaxisalignedbb.f + 1.0D);

        if (this.c(i, k, i1, j, l, j1)) {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        int j2 = this.a(k1, l1, i2);

                        if (j2 == OBlock.ar.ca || j2 == OBlock.C.ca || j2 == OBlock.D.ca) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean a(OAxisAlignedBB oaxisalignedbb, OMaterial omaterial, OEntity oentity) {
        int i = OMathHelper.c(oaxisalignedbb.a);
        int j = OMathHelper.c(oaxisalignedbb.d + 1.0D);
        int k = OMathHelper.c(oaxisalignedbb.b);
        int l = OMathHelper.c(oaxisalignedbb.e + 1.0D);
        int i1 = OMathHelper.c(oaxisalignedbb.c);
        int j1 = OMathHelper.c(oaxisalignedbb.f + 1.0D);

        if (!this.c(i, k, i1, j, l, j1)) {
            return false;
        } else {
            boolean flag = false;
            OVec3 ovec3 = OVec3.a().a(0.0D, 0.0D, 0.0D);

            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = k; l1 < l; ++l1) {
                    for (int i2 = i1; i2 < j1; ++i2) {
                        OBlock oblock = OBlock.m[this.a(k1, l1, i2)];

                        if (oblock != null && oblock.cp == omaterial) {
                            double d0 = (double) ((float) (l1 + 1) - OBlockFluid.d(this.g(k1, l1, i2)));

                            if ((double) l >= d0) {
                                flag = true;
                                oblock.a(this, k1, l1, i2, oentity, ovec3);
                            }
                        }
                    }
                }
            }

            if (ovec3.c() > 0.0D) {
                ovec3 = ovec3.b();
                double d1 = 0.014D;

                oentity.w += ovec3.a * d1;
                oentity.x += ovec3.b * d1;
                oentity.y += ovec3.c * d1;
            }

            return flag;
        }
    }

    public boolean a(OAxisAlignedBB oaxisalignedbb, OMaterial omaterial) {
        int i = OMathHelper.c(oaxisalignedbb.a);
        int j = OMathHelper.c(oaxisalignedbb.d + 1.0D);
        int k = OMathHelper.c(oaxisalignedbb.b);
        int l = OMathHelper.c(oaxisalignedbb.e + 1.0D);
        int i1 = OMathHelper.c(oaxisalignedbb.c);
        int j1 = OMathHelper.c(oaxisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    OBlock oblock = OBlock.m[this.a(k1, l1, i2)];

                    if (oblock != null && oblock.cp == omaterial) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean b(OAxisAlignedBB oaxisalignedbb, OMaterial omaterial) {
        int i = OMathHelper.c(oaxisalignedbb.a);
        int j = OMathHelper.c(oaxisalignedbb.d + 1.0D);
        int k = OMathHelper.c(oaxisalignedbb.b);
        int l = OMathHelper.c(oaxisalignedbb.e + 1.0D);
        int i1 = OMathHelper.c(oaxisalignedbb.c);
        int j1 = OMathHelper.c(oaxisalignedbb.f + 1.0D);

        for (int k1 = i; k1 < j; ++k1) {
            for (int l1 = k; l1 < l; ++l1) {
                for (int i2 = i1; i2 < j1; ++i2) {
                    OBlock oblock = OBlock.m[this.a(k1, l1, i2)];

                    if (oblock != null && oblock.cp == omaterial) {
                        int j2 = this.g(k1, l1, i2);
                        double d0 = (double) (l1 + 1);

                        if (j2 < 8) {
                            d0 = (double) (l1 + 1) - (double) j2 / 8.0D;
                        }

                        if (d0 >= oaxisalignedbb.b) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public OExplosion a(OEntity oentity, double d0, double d1, double d2, float f) {
        return this.a(oentity, d0, d1, d2, f, false);
    }

    public OExplosion a(OEntity oentity, double d0, double d1, double d2, float f, boolean flag) {
        OExplosion oexplosion = new OExplosion(this, oentity, d0, d1, d2, f);

        oexplosion.a = flag;
        oexplosion.a();
        oexplosion.a(true);
        return oexplosion;
    }

    public float a(OVec3 ovec3, OAxisAlignedBB oaxisalignedbb) {
        double d0 = 1.0D / ((oaxisalignedbb.d - oaxisalignedbb.a) * 2.0D + 1.0D);
        double d1 = 1.0D / ((oaxisalignedbb.e - oaxisalignedbb.b) * 2.0D + 1.0D);
        double d2 = 1.0D / ((oaxisalignedbb.f - oaxisalignedbb.c) * 2.0D + 1.0D);
        int i = 0;
        int j = 0;

        for (float f = 0.0F; f <= 1.0F; f = (float) ((double) f + d0)) {
            for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) ((double) f1 + d1)) {
                for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) ((double) f2 + d2)) {
                    double d3 = oaxisalignedbb.a + (oaxisalignedbb.d - oaxisalignedbb.a) * (double) f;
                    double d4 = oaxisalignedbb.b + (oaxisalignedbb.e - oaxisalignedbb.b) * (double) f1;
                    double d5 = oaxisalignedbb.c + (oaxisalignedbb.f - oaxisalignedbb.c) * (double) f2;

                    if (this.a(OVec3.a().a(d3, d4, d5), ovec3) == null) {
                        ++i;
                    }

                    ++j;
                }
            }
        }

        return (float) i / (float) j;
    }

    public boolean a(OEntityPlayer oentityplayer, int i, int j, int k, int l) {
        if (l == 0) {
            --j;
        }

        if (l == 1) {
            ++j;
        }

        if (l == 2) {
            --k;
        }

        if (l == 3) {
            ++k;
        }

        if (l == 4) {
            --i;
        }

        if (l == 5) {
            ++i;
        }

        if (this.a(i, j, k) == OBlock.ar.ca) {
            this.a(oentityplayer, 1004, i, j, k, 0);
            this.e(i, j, k, 0);
            return true;
        } else {
            return false;
        }
    }

    public OTileEntity p(int i, int j, int k) {
        if (j >= 256) {
            return null;
        } else {
            OChunk ochunk = this.e(i >> 4, k >> 4);

            if (ochunk == null) {
                return null;
            } else {
                OTileEntity otileentity = ochunk.e(i & 15, j, k & 15);

                if (otileentity == null) {
                    Iterator iterator = this.a.iterator();

                    while (iterator.hasNext()) {
                        OTileEntity otileentity1 = (OTileEntity) iterator.next();

                        if (!otileentity1.p() && otileentity1.l == i && otileentity1.m == j && otileentity1.n == k) {
                            otileentity = otileentity1;
                            break;
                        }
                    }
                }

                return otileentity;
            }
        }
    }

    public void a(int i, int j, int k, OTileEntity otileentity) {
        if (otileentity != null && !otileentity.p()) {
            if (this.L) {
                otileentity.l = i;
                otileentity.m = j;
                otileentity.n = k;
                this.a.add(otileentity);
            } else {
                this.h.add(otileentity);
                OChunk ochunk = this.e(i >> 4, k >> 4);

                if (ochunk != null) {
                    ochunk.a(i & 15, j, k & 15, otileentity);
                }
            }
        }
    }

    public void q(int i, int j, int k) {
        OTileEntity otileentity = this.p(i, j, k);

        if (otileentity != null && this.L) {
            otileentity.j();
            this.a.remove(otileentity);
        } else {
            if (otileentity != null) {
                this.a.remove(otileentity);
                this.h.remove(otileentity);
            }

            OChunk ochunk = this.e(i >> 4, k >> 4);

            if (ochunk != null) {
                ochunk.f(i & 15, j, k & 15);
            }
        }
    }

    public void a(OTileEntity otileentity) {
        this.b.add(otileentity);
    }

    public boolean r(int i, int j, int k) {
        OBlock oblock = OBlock.m[this.a(i, j, k)];

        return oblock == null ? false : oblock.d();
    }

    public boolean s(int i, int j, int k) {
        return OBlock.i(this.a(i, j, k));
    }

    public boolean t(int i, int j, int k) {
        OBlock oblock = OBlock.m[this.a(i, j, k)];

        return oblock == null ? false : (oblock.cp.k() && oblock.c() ? true : (oblock instanceof OBlockStairs ? (this.g(i, j, k) & 4) == 4 : (oblock instanceof OBlockHalfSlab ? (this.g(i, j, k) & 8) == 8 : false)));
    }

    public boolean b(int i, int j, int k, boolean flag) {
        if (i >= -30000000 && k >= -30000000 && i < 30000000 && k < 30000000) {
            OChunk ochunk = this.y.d(i >> 4, k >> 4);

            if (ochunk != null && !ochunk.g()) {
                OBlock oblock = OBlock.m[this.a(i, j, k)];

                return oblock == null ? false : oblock.cp.k() && oblock.c();
            } else {
                return flag;
            }
        } else {
            return flag;
        }
    }

    public void v() {
        int i = this.a(1.0F);

        if (i != this.k) {
            this.k = i;
        }
    }

    public void a(boolean flag, boolean flag1) {
        this.G = flag;
        this.H = flag1;
    }

    public void b() {
        this.m();
    }

    private void a() {
        if (this.A.o()) {
            this.o = 1.0F;
            if (this.A.m()) {
                this.q = 1.0F;
            }
        }
    }

    protected void m() {
        if (!this.w.e) {
            if (this.r > 0) {
                --this.r;
            }

            int i = this.A.n();

            if (i <= 0) {
                if (this.A.m()) {
                    this.A.f(this.v.nextInt(12000) + 3600);
                } else {
                    this.A.f(this.v.nextInt(168000) + 12000);
                }
            } else {
                --i;
                this.A.f(i);
                if (i <= 0) {
                    // CanaryMod: Thunder hook
                    if (!(Boolean) etc.getLoader().callHook(PluginLoader.Hook.THUNDER_CHANGE, this.world, !this.A.m())) {
                        this.A.a(!this.A.m());
                    } // CanaryMod: diff visibility
                }
            }

            int j = this.A.p();

            if (j <= 0) {
                if (this.A.o()) {
                    this.A.g(this.v.nextInt(12000) + 12000);
                } else {
                    this.A.g(this.v.nextInt(168000) + 12000);
                }
            } else {
                --j;
                this.A.g(j);
                if (j <= 0) {
                    // CanaryMod: Weather hook
                    if (!(Boolean) etc.getLoader().callHook(PluginLoader.Hook.WEATHER_CHANGE, this.world, !this.A.o())) {
                        this.A.b(!this.A.o());
                    } // CanaryMod: diff visibility
                }
            }

            this.n = this.o;
            if (this.A.o()) {
                this.o = (float) ((double) this.o + 0.01D);
            } else {
                this.o = (float) ((double) this.o - 0.01D);
            }

            if (this.o < 0.0F) {
                this.o = 0.0F;
            }

            if (this.o > 1.0F) {
                this.o = 1.0F;
            }

            this.p = this.q;
            if (this.A.m()) {
                this.q = (float) ((double) this.q + 0.01D);
            } else {
                this.q = (float) ((double) this.q - 0.01D);
            }

            if (this.q < 0.0F) {
                this.q = 0.0F;
            }

            if (this.q > 1.0F) {
                this.q = 1.0F;
            }
        }
    }

    public void w() {
        this.A.g(1);
    }

    protected void x() {
        this.I.clear();
        this.F.a("buildList");

        int i;
        OEntityPlayer oentityplayer;
        int j;
        int k;

        for (i = 0; i < this.i.size(); ++i) {
            oentityplayer = (OEntityPlayer) this.i.get(i);
            j = OMathHelper.c(oentityplayer.t / 16.0D);
            k = OMathHelper.c(oentityplayer.v / 16.0D);
            byte b0 = 7;

            for (int l = -b0; l <= b0; ++l) {
                for (int i1 = -b0; i1 <= b0; ++i1) {
                    this.I.add(new OChunkCoordIntPair(l + j, i1 + k));
                }
            }
        }

        this.F.b();
        if (this.M > 0) {
            --this.M;
        }

        this.F.a("playerCheckLight");
        if (!this.i.isEmpty()) {
            i = this.v.nextInt(this.i.size());
            oentityplayer = (OEntityPlayer) this.i.get(i);
            j = OMathHelper.c(oentityplayer.t) + this.v.nextInt(11) - 5;
            k = OMathHelper.c(oentityplayer.u) + this.v.nextInt(11) - 5;
            int j1 = OMathHelper.c(oentityplayer.v) + this.v.nextInt(11) - 5;

            this.x(j, k, j1);
        }

        this.F.b();
    }

    protected void a(int i, int j, OChunk ochunk) {
        this.F.c("moodSound");
        if (this.M == 0) {
            this.l = this.l * 3 + 1013904223;
            int k = this.l >> 2;
            int l = k & 15;
            int i1 = k >> 8 & 15;
            int j1 = k >> 16 & 127;
            int k1 = ochunk.a(l, j1, i1);

            l += i;
            i1 += j;
            if (k1 == 0 && this.k(l, j1, i1) <= this.v.nextInt(8) && this.b(OEnumSkyBlock.a, l, j1, i1) <= 0) {
                OEntityPlayer oentityplayer = this.a((double) l + 0.5D, (double) j1 + 0.5D, (double) i1 + 0.5D, 8.0D);

                if (oentityplayer != null && oentityplayer.e((double) l + 0.5D, (double) j1 + 0.5D, (double) i1 + 0.5D) > 4.0D) {
                    this.a((double) l + 0.5D, (double) j1 + 0.5D, (double) i1 + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.v.nextFloat() * 0.2F);
                    this.M = this.v.nextInt(12000) + 6000;
                }
            }
        }

        this.F.c("checkLight");
        ochunk.o();
    }

    protected void g() {
        this.x();
    }

    public boolean u(int i, int j, int k) {
        return this.c(i, j, k, false);
    }

    public boolean v(int i, int j, int k) {
        return this.c(i, j, k, true);
    }

    public boolean c(int i, int j, int k, boolean flag) {
        OBiomeGenBase obiomegenbase = this.a(i, k);
        float f = obiomegenbase.j();

        if (f > 0.15F) {
            return false;
        } else {
            if (j >= 0 && j < 256 && this.b(OEnumSkyBlock.b, i, j, k) < 10) {
                int l = this.a(i, j, k);

                if ((l == OBlock.B.ca || l == OBlock.A.ca) && this.g(i, j, k) == 0) {
                    if (!flag) {
                        return true;
                    }

                    boolean flag1 = true;

                    if (flag1 && this.f(i - 1, j, k) != OMaterial.g) {
                        flag1 = false;
                    }

                    if (flag1 && this.f(i + 1, j, k) != OMaterial.g) {
                        flag1 = false;
                    }

                    if (flag1 && this.f(i, j, k - 1) != OMaterial.g) {
                        flag1 = false;
                    }

                    if (flag1 && this.f(i, j, k + 1) != OMaterial.g) {
                        flag1 = false;
                    }

                    if (!flag1) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public boolean w(int i, int j, int k) {
        OBiomeGenBase obiomegenbase = this.a(i, k);
        float f = obiomegenbase.j();

        if (f > 0.15F) {
            return false;
        } else {
            if (j >= 0 && j < 256 && this.b(OEnumSkyBlock.b, i, j, k) < 10) {
                int l = this.a(i, j - 1, k);
                int i1 = this.a(i, j, k);

                if (i1 == 0 && OBlock.aS.b(this, i, j, k) && l != 0 && l != OBlock.aT.ca && OBlock.m[l].cp.c()) {
                    return true;
                }
            }

            return false;
        }
    }

    public void x(int i, int j, int k) {
        if (!this.w.e) {
            this.c(OEnumSkyBlock.a, i, j, k);
        }

        this.c(OEnumSkyBlock.b, i, j, k);
    }

    private int a(int i, int j, int k, int l, int i1, int j1) {
        int k1 = 0;

        if (this.j(j, k, l)) {
            k1 = 15;
        } else {
            if (j1 == 0) {
                j1 = 1;
            }

            int l1 = this.b(OEnumSkyBlock.a, j - 1, k, l) - j1;
            int i2 = this.b(OEnumSkyBlock.a, j + 1, k, l) - j1;
            int j2 = this.b(OEnumSkyBlock.a, j, k - 1, l) - j1;
            int k2 = this.b(OEnumSkyBlock.a, j, k + 1, l) - j1;
            int l2 = this.b(OEnumSkyBlock.a, j, k, l - 1) - j1;
            int i3 = this.b(OEnumSkyBlock.a, j, k, l + 1) - j1;

            if (l1 > k1) {
                k1 = l1;
            }

            if (i2 > k1) {
                k1 = i2;
            }

            if (j2 > k1) {
                k1 = j2;
            }

            if (k2 > k1) {
                k1 = k2;
            }

            if (l2 > k1) {
                k1 = l2;
            }

            if (i3 > k1) {
                k1 = i3;
            }
        }

        return k1;
    }

    private int f(int i, int j, int k, int l, int i1, int j1) {
        int k1 = OBlock.q[i1];
        int l1 = this.b(OEnumSkyBlock.b, j - 1, k, l) - j1;
        int i2 = this.b(OEnumSkyBlock.b, j + 1, k, l) - j1;
        int j2 = this.b(OEnumSkyBlock.b, j, k - 1, l) - j1;
        int k2 = this.b(OEnumSkyBlock.b, j, k + 1, l) - j1;
        int l2 = this.b(OEnumSkyBlock.b, j, k, l - 1) - j1;
        int i3 = this.b(OEnumSkyBlock.b, j, k, l + 1) - j1;

        if (l1 > k1) {
            k1 = l1;
        }

        if (i2 > k1) {
            k1 = i2;
        }

        if (j2 > k1) {
            k1 = j2;
        }

        if (k2 > k1) {
            k1 = k2;
        }

        if (l2 > k1) {
            k1 = l2;
        }

        if (i3 > k1) {
            k1 = i3;
        }

        return k1;
    }

    public void c(OEnumSkyBlock oenumskyblock, int i, int j, int k) {
        if (this.a(i, j, k, 17)) {
            int l = 0;
            int i1 = 0;

            this.F.a("getBrightness");
            int j1 = this.b(oenumskyblock, i, j, k);
            boolean flag = false;
            int k1 = this.a(i, j, k);
            int l1 = this.b(i, j, k);

            if (l1 == 0) {
                l1 = 1;
            }

            boolean flag1 = false;
            int i2;

            if (oenumskyblock == OEnumSkyBlock.a) {
                i2 = this.a(j1, i, j, k, k1, l1);
            } else {
                i2 = this.f(j1, i, j, k, k1, l1);
            }

            int j2;
            int k2;
            int l2;
            int i3;
            int j3;
            int k3;
            int l3;
            int i4;

            if (i2 > j1) {
                this.J[i1++] = 133152;
            } else if (i2 < j1) {
                if (oenumskyblock != OEnumSkyBlock.b) {
                    ;
                }

                this.J[i1++] = 133152 + (j1 << 18);

                while (l < i1) {
                    k1 = this.J[l++];
                    l1 = (k1 & 63) - 32 + i;
                    i2 = (k1 >> 6 & 63) - 32 + j;
                    j2 = (k1 >> 12 & 63) - 32 + k;
                    k2 = k1 >> 18 & 15;
                    l2 = this.b(oenumskyblock, l1, i2, j2);
                    if (l2 == k2) {
                        this.b(oenumskyblock, l1, i2, j2, 0);
                        if (k2 > 0) {
                            i3 = l1 - i;
                            k3 = i2 - j;
                            j3 = j2 - k;
                            if (i3 < 0) {
                                i3 = -i3;
                            }

                            if (k3 < 0) {
                                k3 = -k3;
                            }

                            if (j3 < 0) {
                                j3 = -j3;
                            }

                            if (i3 + k3 + j3 < 17) {
                                for (i4 = 0; i4 < 6; ++i4) {
                                    l3 = i4 % 2 * 2 - 1;
                                    int j4 = l1 + i4 / 2 % 3 / 2 * l3;
                                    int k4 = i2 + (i4 / 2 + 1) % 3 / 2 * l3;
                                    int l4 = j2 + (i4 / 2 + 2) % 3 / 2 * l3;

                                    l2 = this.b(oenumskyblock, j4, k4, l4);
                                    int i5 = OBlock.o[this.a(j4, k4, l4)];

                                    if (i5 == 0) {
                                        i5 = 1;
                                    }

                                    if (l2 == k2 - i5 && i1 < this.J.length) {
                                        this.J[i1++] = j4 - i + 32 + (k4 - j + 32 << 6) + (l4 - k + 32 << 12) + (k2 - i5 << 18);
                                    }
                                }
                            }
                        }
                    }
                }

                l = 0;
            }

            this.F.b();
            this.F.a("tcp < tcc");

            while (l < i1) {
                k1 = this.J[l++];
                l1 = (k1 & 63) - 32 + i;
                i2 = (k1 >> 6 & 63) - 32 + j;
                j2 = (k1 >> 12 & 63) - 32 + k;
                k2 = this.b(oenumskyblock, l1, i2, j2);
                l2 = this.a(l1, i2, j2);
                i3 = OBlock.o[l2];
                if (i3 == 0) {
                    i3 = 1;
                }

                boolean flag2 = false;

                if (oenumskyblock == OEnumSkyBlock.a) {
                    k3 = this.a(k2, l1, i2, j2, l2, i3);
                } else {
                    k3 = this.f(k2, l1, i2, j2, l2, i3);
                }

                if (k3 != k2) {
                    this.b(oenumskyblock, l1, i2, j2, k3);
                    if (k3 > k2) {
                        j3 = l1 - i;
                        i4 = i2 - j;
                        l3 = j2 - k;
                        if (j3 < 0) {
                            j3 = -j3;
                        }

                        if (i4 < 0) {
                            i4 = -i4;
                        }

                        if (l3 < 0) {
                            l3 = -l3;
                        }

                        if (j3 + i4 + l3 < 17 && i1 < this.J.length - 6) {
                            if (this.b(oenumskyblock, l1 - 1, i2, j2) < k3) {
                                this.J[i1++] = l1 - 1 - i + 32 + (i2 - j + 32 << 6) + (j2 - k + 32 << 12);
                            }

                            if (this.b(oenumskyblock, l1 + 1, i2, j2) < k3) {
                                this.J[i1++] = l1 + 1 - i + 32 + (i2 - j + 32 << 6) + (j2 - k + 32 << 12);
                            }

                            if (this.b(oenumskyblock, l1, i2 - 1, j2) < k3) {
                                this.J[i1++] = l1 - i + 32 + (i2 - 1 - j + 32 << 6) + (j2 - k + 32 << 12);
                            }

                            if (this.b(oenumskyblock, l1, i2 + 1, j2) < k3) {
                                this.J[i1++] = l1 - i + 32 + (i2 + 1 - j + 32 << 6) + (j2 - k + 32 << 12);
                            }

                            if (this.b(oenumskyblock, l1, i2, j2 - 1) < k3) {
                                this.J[i1++] = l1 - i + 32 + (i2 - j + 32 << 6) + (j2 - 1 - k + 32 << 12);
                            }

                            if (this.b(oenumskyblock, l1, i2, j2 + 1) < k3) {
                                this.J[i1++] = l1 - i + 32 + (i2 - j + 32 << 6) + (j2 + 1 - k + 32 << 12);
                            }
                        }
                    }
                }
            }

            this.F.b();
        }
    }

    public boolean a(boolean flag) {
        return false;
    }

    public List a(OChunk ochunk, boolean flag) {
        return null;
    }

    public List b(OEntity oentity, OAxisAlignedBB oaxisalignedbb) {
        this.N.clear();
        int i = OMathHelper.c((oaxisalignedbb.a - 2.0D) / 16.0D);
        int j = OMathHelper.c((oaxisalignedbb.d + 2.0D) / 16.0D);
        int k = OMathHelper.c((oaxisalignedbb.c - 2.0D) / 16.0D);
        int l = OMathHelper.c((oaxisalignedbb.f + 2.0D) / 16.0D);

        for (int i1 = i; i1 <= j; ++i1) {
            for (int j1 = k; j1 <= l; ++j1) {
                if (this.c(i1, j1)) {
                    this.e(i1, j1).a(oentity, oaxisalignedbb, this.N);
                }
            }
        }

        return this.N;
    }

    public List a(Class oclass, OAxisAlignedBB oaxisalignedbb) {
        int i = OMathHelper.c((oaxisalignedbb.a - 2.0D) / 16.0D);
        int j = OMathHelper.c((oaxisalignedbb.d + 2.0D) / 16.0D);
        int k = OMathHelper.c((oaxisalignedbb.c - 2.0D) / 16.0D);
        int l = OMathHelper.c((oaxisalignedbb.f + 2.0D) / 16.0D);
        ArrayList arraylist = new ArrayList();

        for (int i1 = i; i1 <= j; ++i1) {
            for (int j1 = k; j1 <= l; ++j1) {
                if (this.c(i1, j1)) {
                    this.e(i1, j1).a(oclass, oaxisalignedbb, arraylist);
                }
            }
        }

        return arraylist;
    }

    public OEntity a(Class oclass, OAxisAlignedBB oaxisalignedbb, OEntity oentity) {
        List list = this.a(oclass, oaxisalignedbb);
        OEntity oentity1 = null;
        double d0 = Double.MAX_VALUE;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            OEntity oentity2 = (OEntity) iterator.next();

            if (oentity2 != oentity) {
                double d1 = oentity.e(oentity2);

                if (d1 <= d0) {
                    oentity1 = oentity2;
                    d0 = d1;
                }
            }
        }

        return oentity1;
    }

    public void b(int i, int j, int k, OTileEntity otileentity) {
        if (this.e(i, j, k)) {
            this.d(i, k).e();
        }
    }

    public int a(Class oclass) {
        int i = 0;

        for (int j = 0; j < this.f.size(); ++j) {
            OEntity oentity = (OEntity) this.f.get(j);

            if (oclass.isAssignableFrom(oentity.getClass())) {
                ++i;
            }
        }

        return i;
    }

    public void a(List list) {
        this.f.addAll(list);

        for (int i = 0; i < list.size(); ++i) {
            this.a((OEntity) list.get(i));
        }
    }

    public void b(List list) {
        this.g.addAll(list);
    }

    public boolean a(int i, int j, int k, int l, boolean flag, int i1, OEntity oentity) {
        int j1 = this.a(j, k, l);
        OBlock oblock = OBlock.m[j1];
        OBlock oblock1 = OBlock.m[i];
        OAxisAlignedBB oaxisalignedbb = oblock1.e(this, j, k, l);

        if (flag) {
            oaxisalignedbb = null;
        }

        if (oaxisalignedbb != null && !this.a(oaxisalignedbb, oentity)) {
            return false;
        } else {
            if (oblock != null && (oblock == OBlock.A || oblock == OBlock.B || oblock == OBlock.C || oblock == OBlock.D || oblock == OBlock.ar || oblock.cp.j())) {
                oblock = null;
            }

            return i > 0 && oblock == null && oblock1.a_(this, j, k, l, i1);
        }
    }

    public OPathEntity a(OEntity oentity, OEntity oentity1, float f, boolean flag, boolean flag1, boolean flag2, boolean flag3) {
        this.F.a("pathfind");
        int i = OMathHelper.c(oentity.t);
        int j = OMathHelper.c(oentity.u + 1.0D);
        int k = OMathHelper.c(oentity.v);
        int l = (int) (f + 16.0F);
        int i1 = i - l;
        int j1 = j - l;
        int k1 = k - l;
        int l1 = i + l;
        int i2 = j + l;
        int j2 = k + l;
        OChunkCache ochunkcache = new OChunkCache(this, i1, j1, k1, l1, i2, j2);
        OPathEntity opathentity = (new OPathFinder(ochunkcache, flag, flag1, flag2, flag3)).a(oentity, oentity1, f);

        this.F.b();
        return opathentity;
    }

    public OPathEntity a(OEntity oentity, int i, int j, int k, float f, boolean flag, boolean flag1, boolean flag2, boolean flag3) {
        this.F.a("pathfind");
        int l = OMathHelper.c(oentity.t);
        int i1 = OMathHelper.c(oentity.u);
        int j1 = OMathHelper.c(oentity.v);
        int k1 = (int) (f + 8.0F);
        int l1 = l - k1;
        int i2 = i1 - k1;
        int j2 = j1 - k1;
        int k2 = l + k1;
        int l2 = i1 + k1;
        int i3 = j1 + k1;
        OChunkCache ochunkcache = new OChunkCache(this, l1, i2, j2, k2, l2, i3);
        OPathEntity opathentity = (new OPathFinder(ochunkcache, flag, flag1, flag2, flag3)).a(oentity, i, j, k, f);

        this.F.b();
        return opathentity;
    }

    public boolean k(int i, int j, int k, int l) {
        int i1 = this.a(i, j, k);

        return i1 == 0 ? false : OBlock.m[i1].c(this, i, j, k, l);
    }

    public boolean y(int i, int j, int k) {
        return this.k(i, j - 1, k, 0) ? true : (this.k(i, j + 1, k, 1) ? true : (this.k(i, j, k - 1, 2) ? true : (this.k(i, j, k + 1, 3) ? true : (this.k(i - 1, j, k, 4) ? true : this.k(i + 1, j, k, 5)))));
    }

    public boolean l(int i, int j, int k, int l) {
        if (this.s(i, j, k)) {
            return this.y(i, j, k);
        } else {
            int i1 = this.a(i, j, k);

            return i1 == 0 ? false : OBlock.m[i1].a((OIBlockAccess) this, i, j, k, l);
        }
    }

    public boolean z(int i, int j, int k) {
        return this.l(i, j - 1, k, 0) ? true : (this.l(i, j + 1, k, 1) ? true : (this.l(i, j, k - 1, 2) ? true : (this.l(i, j, k + 1, 3) ? true : (this.l(i - 1, j, k, 4) ? true : this.l(i + 1, j, k, 5)))));
    }

    public OEntityPlayer a(OEntity oentity, double d0) {
        return this.a(oentity.t, oentity.u, oentity.v, d0);
    }

    public OEntityPlayer a(double d0, double d1, double d2, double d3) {
        double d4 = -1.0D;
        OEntityPlayer oentityplayer = null;

        for (int i = 0; i < this.i.size(); ++i) {
            OEntityPlayer oentityplayer1 = (OEntityPlayer) this.i.get(i);
            double d5 = oentityplayer1.e(d0, d1, d2);

            if ((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1.0D || d5 < d4)) {
                d4 = d5;
                oentityplayer = oentityplayer1;
            }
        }

        return oentityplayer;
    }

    public OEntityPlayer b(OEntity oentity, double d0) {
        return this.b(oentity.t, oentity.u, oentity.v, d0);
    }

    public OEntityPlayer b(double d0, double d1, double d2, double d3) {
        double d4 = -1.0D;
        OEntityPlayer oentityplayer = null;

        for (int i = 0; i < this.i.size(); ++i) {
            OEntityPlayer oentityplayer1 = (OEntityPlayer) this.i.get(i);

            if (!oentityplayer1.bZ.a) {
                double d5 = oentityplayer1.e(d0, d1, d2);

                if ((d3 < 0.0D || d5 < d3 * d3) && (d4 == -1.0D || d5 < d4)) {
                    d4 = d5;
                    oentityplayer = oentityplayer1;
                }
            }
        }

        return oentityplayer;
    }

    public OEntityPlayer a(String s) {
        for (int i = 0; i < this.i.size(); ++i) {
            if (s.equals(((OEntityPlayer) this.i.get(i)).bJ)) {
                return (OEntityPlayer) this.i.get(i);
            }
        }

        return null;
    }

    public void B() {
        this.z.c();
    }

    public void b(long i) {
        if (!(Boolean) etc.getLoader().callHook(PluginLoader.Hook.TIME_CHANGE, this.world, i)) {
            this.A.b(i);
        } // CanaryMod: diff visibility
    }

    public long C() {
        return this.A.b();
    }

    public long D() {
        return this.A.f();
    }

    public OChunkCoordinates E() {
        return new OChunkCoordinates(this.A.c(), this.A.d(), this.A.e());
    }

    public boolean a(OEntityPlayer oentityplayer, int i, int j, int k) {
        return true;
    }

    public void a(OEntity oentity, byte b0) {}

    public OIChunkProvider F() {
        return this.y;
    }

    public void b(int i, int j, int k, int l, int i1, int j1) {
        if (l > 0) {
            OBlock.m[l].b(this, i, j, k, i1, j1);
        }
    }

    public OISaveHandler G() {
        return this.z;
    }

    public OWorldInfo H() {
        return this.A;
    }

    public void c() {}

    public float i(float f) {
        return (this.p + (this.q - this.p) * f) * this.j(f);
    }

    public float j(float f) {
        return this.n + (this.o - this.n) * f;
    }

    public boolean I() {
        return (double) this.i(1.0F) > 0.9D;
    }

    public boolean J() {
        return (double) this.j(1.0F) > 0.2D;
    }

    public boolean B(int i, int j, int k) {
        if (!this.J()) {
            return false;
        } else if (!this.j(i, j, k)) {
            return false;
        } else if (this.g(i, k) > j) {
            return false;
        } else {
            OBiomeGenBase obiomegenbase = this.a(i, k);

            return obiomegenbase.c() ? false : obiomegenbase.d();
        }
    }

    public boolean C(int i, int j, int k) {
        OBiomeGenBase obiomegenbase = this.a(i, k);

        return obiomegenbase.e();
    }

    public void a(String s, OWorldSavedData oworldsaveddata) {
        this.C.a(s, oworldsaveddata);
    }

    public OWorldSavedData a(Class oclass, String s) {
        return this.C.a(oclass, s);
    }

    public int b(String s) {
        return this.C.a(s);
    }

    public void e(int i, int j, int k, int l, int i1) {
        this.a((OEntityPlayer) null, i, j, k, l, i1);
    }

    public void a(OEntityPlayer oentityplayer, int i, int j, int k, int l, int i1) {
        for (int j1 = 0; j1 < this.x.size(); ++j1) {
            ((OIWorldAccess) this.x.get(j1)).a(oentityplayer, i, j, k, l, i1);
        }
    }

    public int K() {
        return 256;
    }

    public int L() {
        return this.w.e ? 128 : 256;
    }

    public Random D(int i, int j, int k) {
        long l = (long) i * 341873128712L + (long) j * 132897987541L + this.H().b() + (long) k;

        this.v.setSeed(l);
        return this.v;
    }

    public boolean M() {
        return false;
    }

    public OChunkPosition b(String s, int i, int j, int k) {
        return this.F().a(this, s, i, j, k);
    }

    public OCrashReport a(OCrashReport ocrashreport) {
        ocrashreport.a("World " + this.A.j() + " Entities", (Callable) (new OCallableLvl1(this)));
        ocrashreport.a("World " + this.A.j() + " Players", (Callable) (new OCallableLvl2(this)));
        ocrashreport.a("World " + this.A.j() + " Chunk Stats", (Callable) (new OCallableLvl3(this)));
        return ocrashreport;
    }

    public void f(int i, int j, int k, int l, int i1) {
        Iterator iterator = this.x.iterator();

        while (iterator.hasNext()) {
            OIWorldAccess oiworldaccess = (OIWorldAccess) iterator.next();

            oiworldaccess.a(i, j, k, l, i1);
        }
    }
}
