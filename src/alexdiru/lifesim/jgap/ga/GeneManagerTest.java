package alexdiru.lifesim.jgap.ga;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.jgap.*;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.IntegerGene;
import org.junit.Test;

public class GeneManagerTest extends TestCase {
    @SuppressWarnings("deprecation")
	@Test
	public void testSavingAndLoading() throws InvalidConfigurationException {
		IntegerGene[] genes = new IntegerGene[4];
		Configuration config = new DefaultConfiguration();
		for (int i = 0; i < 4; i++) {
			genes[i] = new IntegerGene(config);
			genes[i].setAllele(i);
		}
		
		GeneManager.saveToFile("test.txt", genes);
		Gene[] loadedGenes = GeneManager.loadFromFile(config, "test.txt");

		Assert.assertTrue(loadedGenes[0].getAllele().equals(genes[0].getAllele()));
		Assert.assertTrue(loadedGenes[1].getAllele().equals(genes[1].getAllele()));
		Assert.assertTrue(loadedGenes[2].getAllele().equals(genes[2].getAllele()));
		Assert.assertTrue(loadedGenes[3].getAllele().equals(genes[3].getAllele()));
	}

    @Test
    public void testCountUniqueGenes() throws InvalidConfigurationException {
        GAConfiguration configuration = new GAConfiguration();
        Gene[] a = new Gene[] { new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration) };
        Gene[] b = new Gene[] { new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration) };
        Gene[] c = new Gene[] { new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration) };
        Gene[] d = new Gene[] { new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration) };
        Gene[] e = new Gene[] { new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration) };
        Gene[] f = new Gene[] { new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration) };

        a[0].setAllele(1);
        b[0].setAllele(1);
        c[0].setAllele(1);
        a[1].setAllele(6);
        b[1].setAllele(6);
        c[1].setAllele(6);
        a[2].setAllele(7);
        b[2].setAllele(7);
        c[2].setAllele(7);
        a[3].setAllele(2);
        b[3].setAllele(2);
        c[3].setAllele(1);
        a[4].setAllele(4);
        b[4].setAllele(4);
        c[4].setAllele(4);
        d[0].setAllele(1);
        e[0].setAllele(1);
        f[0].setAllele(1);
        d[1].setAllele(6);
        e[1].setAllele(6);
        f[1].setAllele(6);
        d[2].setAllele(7);
        e[2].setAllele(7);
        f[2].setAllele(7);
        d[3].setAllele(2);
        e[3].setAllele(2);
        f[3].setAllele(1);
        d[4].setAllele(4);
        e[4].setAllele(4);
        f[4].setAllele(4);

        Chromosome A = new Chromosome(configuration);
        A.setGenes(a);
        Chromosome B = new Chromosome(configuration);
        B.setGenes(b);
        Chromosome C = new Chromosome(configuration);
        C.setGenes(c);
        Chromosome D = new Chromosome(configuration);
        D.setGenes(d);
        Chromosome E = new Chromosome(configuration);
        E.setGenes(e);
        Chromosome F = new Chromosome(configuration);
        F.setGenes(f);

        Population p1 = new Population(configuration);
        p1.addChromosome(A);
        p1.addChromosome(B);
        p1.addChromosome(C);

        Assert.assertTrue(GeneManager.countUniqueGenes(p1) == 2);

        p1.addChromosome(D);
        p1.addChromosome(E);
        p1.addChromosome(F);

        Assert.assertTrue(GeneManager.countUniqueGenes(p1) == 2);

        D.getGene(3).setAllele(100);

        Assert.assertTrue(GeneManager.countUniqueGenes(p1) == 3);
    }

    @Test
    public void testGeneEquality() throws InvalidConfigurationException {
        GAConfiguration configuration = new GAConfiguration();
        Gene[] a = new Gene[] { new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration) };
        Gene[] b = new Gene[] { new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration) };
        Gene[] c = new Gene[] { new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration), new IntegerGene(configuration) };

        a[0].setAllele(1);
        b[0].setAllele(1);
        c[0].setAllele(1);
        a[1].setAllele(6);
        b[1].setAllele(6);
        c[1].setAllele(6);
        a[2].setAllele(7);
        b[2].setAllele(7);
        c[2].setAllele(7);
        a[3].setAllele(2);
        b[3].setAllele(2);
        c[3].setAllele(1);
        a[4].setAllele(4);
        b[4].setAllele(4);
        c[4].setAllele(4);

        Assert.assertTrue(GeneManager.geneEquality(a,b));
        Assert.assertTrue(GeneManager.geneEquality(b,a));
        Assert.assertTrue(!GeneManager.geneEquality(b,c));
        Assert.assertTrue(!GeneManager.geneEquality(c,a));

    }
}
