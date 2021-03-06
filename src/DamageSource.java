/**
 * A basic DamageSource
 * @author chris
 *
 */
public class DamageSource {

    protected ODamageSource osource;

    /**
     * Create a new CanaryMod DamageSource from an ODamageSource
     * @param osource
     */
    public DamageSource(ODamageSource osource) {
        this.osource = osource;
    }

    /**
     * Get the death message for the player that will be displayed in chat,
     * if the player dies from this {@link DamageSource}.
     * @param player
     * @return
     */
    public String getDeathMessage(Player player) {
        // SRG return osource.func_76360_b(player.getEntity()).func_111062_i();
        return osource.b(player.getEntity()).i();
    }

    /**
     * Get the name of the damage type as defined
     * when the underlying ODamageSource was created.
     * @return
     */
    public String getName() {
        // SRG return osource.field_76373_n;
        return osource.o;
    }

    /**
     * Get the <tt>BaseEntity</tt> that is the root of the damage.
     * For example, if a player shoots an arrow, this returns the player.
     * This may return null if the the damage was not caused by an entity.
     * @return the source entity
     */
    public BaseEntity getSourceEntity() {
        // SRG return osource.func_76346_g() != null ? osource.func_76346_g().getEntity() : null;
        return osource.i() != null ? osource.i().getEntity() : null;
    }

    /**
     * Get the <tt>BaseEntity</tt> that is causing the damage.
     * For example, if a player shoots an arrow, this returns the arrow.
     * This may return null if the the damage was not caused by an entity.
     * @return the damaging entity
     */
    public BaseEntity getDamagingEntity() {
        // SRG return osource.func_76364_f() != null ? osource.func_76364_f().getEntity() : null;
        return osource.h() != null ? osource.h().getEntity() : null;
    }

    /**
     * Checks if this DamageSource is a projectile.
     * @return true if projectile, false otherwise
     */
    public boolean isProjectileDamage() {
        // SRG return osource.func_76352_a();
        return osource.a();
    }

    /**
     * Mark this DamageSource as projectile and return an instance.
     * @return this
     */
    public DamageSource setProjectileDamage() {
        // SRG osource.func_76349_b();
        osource.b();
        return this;
    }

    /**
     * Checks if this DamageSOurce is an explosion.
     * @return true if is explosion, false otherwise
     */
    public boolean isExplosionDamage() {
        // SRG return osource.func_94541_c();
        return osource.c();
    }

    /**
     * Mark this DamageSource as explosion and return an instance.
     * @return this
     */
    public DamageSource setExplosionDamage() {
        // SRG osource.func_94540_d();
        osource.d();
        return this;
    }

    /**
     * Check if this is fire damage
     * @return true if fire damage, false otherwise
     */
    public boolean isFireDamage() {
        // SRG return osource.func_76347_k();
        return osource.m();
    }

    /**
     * Check if this is magic damage.
     * @return true if magic damage, false otherwise
     */
    public boolean isMagicDamage() {
        // SRG return osource.func_82725_o();
        return osource.q();
    }

    /**
     * Mark this DamageSource as magic damage and return an instance
     * @return this
     */
    public DamageSource setMagicDamage() {
        // SRG osource.func_82726_p();
        osource.r();
        return this;
    }
    /**
     * Returns true when this damage source cannot be blocked.
     * @return
     */
    public boolean isUnblockable() {
        // SRG return osource.func_76363_c();
        return osource.e();
    }

    /**
     * Returns true when this damage also applies in creative-mode
     * @return
     */
    public boolean canDamageInCreativeMode() {
        // SRG return osource.func_76357_e();
        return osource.g();
    }

    /**
     * Check if this DamageSource is caused by an entity
     * @return
     */
    public boolean isEntityDamageSource() {
        return osource instanceof OEntityDamageSource;
    }

    /**
     * Check if this DamageSource has an indirect cause (magic, explosions etc)
     * @return
     */
    public boolean isIndirectDamageSource() {
        return osource instanceof OEntityDamageSourceIndirect;
    }

    /**
     * Returns the amount of hunger that will be removed upon damage infliction.
     * @return
     */
    public float getHungerImpact() {
        // SRG return osource.func_76345_d();
        return osource.f();
    }

    /**
     * Check if the damage done will be scaled down/up by difficulty level
     * @return
     */
    public boolean isDifficultyScaled() {
        // SRG return osource.func_76350_n();
        return osource.p();
    }

    /**
     * Make this DamageSource dependent on difficulty level
     * @return
     */
    public DamageSource setDifficultyScaled() {
        // SRG osource.func_76351_m();
        osource.o();
        return this;
    }

    /**
     * Returns the native ODamageSource referenec
     * @return
     */
    public ODamageSource getDamageSource() {
        return osource;
    }

    public DamageType getDamageType() {
        return DamageType.fromDamageSource(this);
    }

    // Static util methods

    /**
     * Create a DamageSource for the given LivingEntity.
     * @param entity LivingEntity that causes the damage
     * @return {@link EntityDamageSource}
     */
    public static DamageSource createMobDamage(LivingEntity entity) {
        return ODamageSource.a(entity.getEntity()).damageSource;
    }

    /**
     * Create a DamageSource for the given Player.
     * @param player Player that causes the damage
     * @return {@link EntityDamageSource}
     */
    public static DamageSource createPlayerDamage(Player player) {
        return ODamageSource.a(player.getEntity()).damageSource;
    }

    /**
     * Creates a thorn-enchantment DamageSource that is caused by the given entity
     * @param entity
     * @return
     */
    public static DamageSource createThornsDamage(BaseEntity entity) {
        return ODamageSource.a(entity.getEntity()).damageSource;
    }
}
