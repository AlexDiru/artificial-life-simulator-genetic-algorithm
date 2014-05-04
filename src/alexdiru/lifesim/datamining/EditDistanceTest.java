package alexdiru.lifesim.datamining;

import convenience.RTED;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 19/02/14
 * Time: 20:03
 * To change this template use File | Settings | File Templates.
 */
public class EditDistanceTest extends TestCase {

    @Test
    public void testMaxEditDistance() {
        /* Max edit distance of depth = 3 is 3
           Max edit distance of depth = 2 is 2
            There max edit distance is depth of tree
         */
        String a = "{ a { b } { c } }";
        String b = "{ d { e } { f } }";
        String c = "{ a { b { g } { h } } { c { i } { j } } }";
        String d = "{ d { e { k } { l } } { f { m } { n } } }";
        String e = "{ 5 }";
        String f = "{ 6 }";
        String g = "{ a { b { g { o } { p } } { h { q } { r } } } { c { i { s } { t } } { j { u } { v } } } }";
        String h = "{ d { e { k { w } { x } } { l { y } { z } } } { f { m { 1 } { 2 } } { n { 3 } { 4 } } } }";
        System.out.println("AB = " + RTED.computeDistance(a,b));
        System.out.println("CD = " + RTED.computeDistance(c,d));
        System.out.println("EF = " + RTED.computeDistance(e,f));
        System.out.println("GH = " + RTED.computeDistance(g,h));
        System.out.println("GE = " + RTED.computeDistance(g,e));



        String i = "{2{11{12{14}{0}}{2{14}{13}}}{4{13{7}{12}}{6{14}{13}}}}";
        String j = "{2{11{12{7}{13}}{2{11}{13}}}{4{0{11}{14}}{6{14}{13}}}}";
        String k = "{2{a{b{d}{0}}{2{d}{c}}}{4{c{7}{b}}{6{e}}}}";
        String l = "{2{a{b{7}{c}}{2{a}{c}}}{4{0{a}{d}}{6{d}{c}}}}";
        System.out.println("IJ = " + RTED.computeDistance(i,j));
        System.out.println("KL = " + RTED.computeDistance(k,l));

    }
}
