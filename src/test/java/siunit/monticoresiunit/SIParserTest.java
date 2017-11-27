/**
 *
 *  ******************************************************************************
 *  MontiCAR Modeling Family, www.se-rwth.de
 *  Copyright (c) 2017, Software Engineering Group at RWTH Aachen,
 *  All rights reserved.
 *
 *  This project is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 3.0 of the License, or (at your option) any later version.
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this project. If not, see <http://www.gnu.org/licenses/>.
 * *******************************************************************************
 */
package siunit.monticoresiunit;

import siunit.monticoresiunit.si._ast.ASTComplexNumber;
import siunit.monticoresiunit.si._ast.ASTNumber;
import siunit.monticoresiunit.si._ast.ASTUnitNumber;
import siunit.monticoresiunit.si._parser.SIParser;
import de.se_rwth.commons.logging.Log;
import org.jscience.mathematics.number.Rational;
import org.junit.*;

import javax.measure.unit.Unit;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by michaelvonwenckstern on 10.02.17.
 */
public class SIParserTest {

    static boolean failQuick;

    @BeforeClass
    public static void startUp() {
        failQuick = Log.isFailQuickEnabled();
        Log.enableFailQuick(false);
    }

    @AfterClass
    public static void tearDown() {
        Log.enableFailQuick(failQuick);
    }

    @Before
    public void clear() {
        Log.getFindings().clear();
    }
	
	@Test
    public void testDegree20() throws IOException {
        SIParser parser = new SIParser();
        ASTNumber ast = parser.parse_String("7°").orElse(null);
        assertNotNull(ast);
	}

    @Test
    public void testM2() throws IOException {
        SIParser parser = new SIParser();
        ASTUnitNumber ast = parser.parseString_UnitNumber("-0.9").orElse(null);
        assertNotNull(ast);

        assertEquals(Rational.valueOf(-9, 10), ast.getNumber().get());
        assertEquals(Unit.ONE, ast.getUnit().get());
    }

    @Test
    public void testM1() throws IOException {
        SIParser parser = new SIParser();
        ASTUnitNumber ast = parser.parseString_UnitNumber("-0.5 kg*m^2/s^3").orElse(null);
        assertNotNull(ast);

        assertEquals(Rational.valueOf(-1, 2), ast.getNumber().get());
        assertEquals(Unit.valueOf("kg*m^2/s^3"), ast.getUnit().get());
    }

    @Test
    public void test0() throws IOException {
        SIParser parser = new SIParser();
        ASTUnitNumber ast = parser.parseString_UnitNumber("8/3 kg*m^2/s^3").orElse(null);
        assertNotNull(ast);

        assertEquals(Rational.valueOf(8, 3), ast.getNumber().get());
        assertEquals(Unit.valueOf("kg*m^2/s^3"), ast.getUnit().get());
    }

    @Test
    public void test1() throws IOException {
        SIParser parser = new SIParser();
        ASTUnitNumber ast = parser.parseString_UnitNumber("8 kg*m^2/s^3").orElse(null);
        assertNotNull(ast);

        assertEquals(Rational.valueOf(8, 1), ast.getNumber().get());
        assertEquals(Unit.valueOf("kg*m^2/s^3"), ast.getUnit().get());
    }


    @Test
    public void test2() {
        SIParser parser = new SIParser();
        try {
            ASTNumber ast = parser.parseString_Number("0.3e-7").get();
        } catch (Exception e) {}
        finally {
            // need to have a parser recognition error on the token TUnitNumber
            assertTrue(Log.getErrorCount() == 0);
        }
    }

    @Test
    public void testHex() {
        SIParser parser = new SIParser();
        try {
            ASTNumber ast = parser.parseString_Number("0xfff").get();
        } catch (Exception e) {}
        finally {
            // need to have a parser recognition error on the token TUnitNumber
            assertTrue(Log.getErrorCount() == 0);
        }
    }

    @Test
    public void testE2() {
        SIParser parser = new SIParser();
        try {
            parser.parseString_UnitNumber("5m/s");
        } catch (Exception e) {}
        finally {
            // need to have a parser recognition error on the token TUnitNumber
            assertTrue(Log.getErrorCount() == 0);
        }
    }

    @Test
    public void testMixedSiNonOfficial() {
        SIParser parser = new SIParser();
        try {
            parser.parseString_UnitNumber("5km/h");
        } catch (Exception e) {}
        finally {
            // need to have a parser recognition error on the token TUnitNumber
            assertTrue(Log.getErrorCount() == 0);
        }
    }

    @Ignore
    @Test
    public void test3() {
        SIParser parser = new SIParser();
        try {
            ASTNumber a = parser.parseString_Number("3.00e+8").get();
        } catch (Exception e) {}
        finally {
            // need to have a parser recognition error on the token TUnitNumber
            assertTrue(Log.getErrorCount() > 0);
        }
    }

    @Test
    public void test4() throws IOException {
        SIParser parser = new SIParser();
        ASTComplexNumber ast = parser.parseString_ComplexNumber("-7/3 -0.5i").orElse(null);
        assertNotNull(ast);

        assertEquals(Rational.valueOf(-7, 3), ast.getReal());
        assertEquals(Rational.valueOf(-1, 2), ast.getImg());
    }

    @Test
    public void test5() throws IOException {
        SIParser parser = new SIParser();
        ASTComplexNumber ast = parser.parseString_ComplexNumber("1-2i").orElse(null);
        assertNotNull(ast);

        assertEquals(Rational.valueOf(1, 1), ast.getReal());
        assertEquals(Rational.valueOf(-2, 1), ast.getImg());
    }

    @Test
    public void test6() throws IOException {
        SIParser parser = new SIParser();
        ASTComplexNumber ast = parser.parseString_ComplexNumber("1 -2i").orElse(null);
        assertNotNull(ast);

        assertEquals(Rational.valueOf(1, 1), ast.getReal());
        assertEquals(Rational.valueOf(-2, 1), ast.getImg());
    }

    @Test
    public void test7() throws IOException {
        SIParser parser = new SIParser();
        ASTComplexNumber ast = parser.parseString_ComplexNumber("1  -  2i").orElse(null);
        assertNotNull(ast);

        assertEquals(Rational.valueOf(1, 1), ast.getReal());
        assertEquals(Rational.valueOf(-2, 1), ast.getImg());
    }

    @Test
    public void test8() throws IOException {
        SIParser parser = new SIParser();
        ASTComplexNumber ast = parser.parseString_ComplexNumber("-7/3 -1/2i").orElse(null);
        assertNotNull(ast);

        assertEquals(Rational.valueOf(-7, 3), ast.getReal());
        assertEquals(Rational.valueOf(-1, 2), ast.getImg());
    }

    @Test
    public void test9() throws IOException {
        SIParser parser = new SIParser();
        ASTComplexNumber ast = parser.parseString_ComplexNumber("-0.5-0.5i").orElse(null);
        assertNotNull(ast);

        assertEquals(Rational.valueOf(-1, 2), ast.getReal());
        assertEquals(Rational.valueOf(-1, 2), ast.getImg());
    }

    @Test
    public void testInfinite() throws IOException {
        SIParser parser = new SIParser();
        ASTNumber ast = parser.parseString_Number("-oo km/h").orElse(null);
        assertNotNull(ast);
        System.out.println(ast);
        System.out.println(ast.getUnitNumber().get().tUnitInfIsPresent());
        System.out.println(ast.getUnitNumber().get().getUnit());
    }

    @Test
    public void testDegree() throws IOException{
        SIParser parser = new SIParser();
        ASTUnitNumber ast = parser.parseString_UnitNumber("-90 °").orElse(null);
        assertNotNull(ast);
        System.out.println(ast);

    }

    @Test
    public void testDegree2() throws IOException{
        SIParser parser = new SIParser();
        ASTUnitNumber ast = parser.parseString_UnitNumber("-90 deg").orElse(null);
        assertNotNull(ast);
        System.out.println(ast);

    }

}
