package net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.release.genlayer;

import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.release.IntCache;
import net.raphimc.vialegacy.protocols.release.protocol1_2_1_3to1_1.biome.release.NewBiomeGenBase;

public class GenLayerDownfallMix extends GenLayer {
    private final GenLayer field_35507_b;
    private final int field_35508_c;

    public GenLayerDownfallMix(GenLayer genlayer, GenLayer genlayer1, int i) {
        super(0L);
        parent = genlayer1;
        field_35507_b = genlayer;
        field_35508_c = i;
    }

    public int[] getInts(int i, int j, int k, int l) {
        int[] ai = parent.getInts(i, j, k, l);
        int[] ai1 = field_35507_b.getInts(i, j, k, l);
        int[] ai2 = IntCache.getIntCache(k * l);
        for (int i1 = 0; i1 < k * l; i1++) {
            ai2[i1] = ai1[i1] + (NewBiomeGenBase.BIOME_LIST[ai[i1]].getIntRainfall() - ai1[i1]) / (field_35508_c + 1);
        }

        return ai2;
    }
}
