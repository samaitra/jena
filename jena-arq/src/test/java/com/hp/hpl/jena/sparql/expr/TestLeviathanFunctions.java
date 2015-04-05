/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hp.hpl.jena.sparql.expr;

import org.apache.jena.atlas.junit.BaseTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.shared.impl.PrefixMappingImpl;
import com.hp.hpl.jena.sparql.ARQConstants;
import com.hp.hpl.jena.sparql.function.FunctionEnvBase;
import com.hp.hpl.jena.sparql.function.library.leviathan.LeviathanConstants;
import com.hp.hpl.jena.sparql.util.ExprUtils;
import com.hp.hpl.jena.sparql.util.NodeFactoryExtra;

public class TestLeviathanFunctions extends BaseTest {

    private static final double DELTA = 0.0000000001d;
    static boolean warnOnBadLexicalForms = true;

    @BeforeClass
    public static void beforeClass() {
        warnOnBadLexicalForms = NodeValue.VerboseWarnings;
        NodeValue.VerboseWarnings = false;
    }

    @AfterClass
    public static void afterClass() {
        NodeValue.VerboseWarnings = warnOnBadLexicalForms;
    }

    private static PrefixMapping pmap = new PrefixMappingImpl();
    static {
        pmap.setNsPrefixes(ARQConstants.getGlobalPrefixMap());
        pmap.setNsPrefix("lfn", LeviathanConstants.LeviathanFunctionLibraryURI);
    }

    @Test
    public void sq_01() {
        test("lfn:sq(2)", "4");
    }

    @Test
    public void sq_02() {
        test("lfn:sq(3)", "9");
    }

    @Test
    public void sq_03() {
        test("lfn:sq(0.5)", "0.25");
    }

    @Test
    public void cube_01() {
        test("lfn:cube(2)", "8");
    }

    @Test
    public void cube_02() {
        test("lfn:cube(3)", "27");
    }

    @Test
    public void cube_03() {
        test("lfn:cube(0.5)", "0.125");
    }

    @Test
    public void e_01() {
        test("lfn:e(2)", NodeFactoryExtra.doubleToNode(Math.exp(2d)));
    }

    @Test
    public void pow_01() {
        test("lfn:pow(2, 4)", "16");
    }

    @Test
    public void pow_02() {
        test("lfn:pow(0.5, 3)", "0.125");
    }

    @Test
    public void factorial_01() {
        test("lfn:factorial(0)", "1");
    }

    @Test
    public void factorial_02() {
        test("lfn:factorial(1)", "1");
    }

    @Test
    public void factorial_03() {
        test("lfn:factorial(3)", "6");
    }

    @Test
    public void factorial_04() {
        test("lfn:factorial(5)", "120");
    }

    @Test(expected = ExprEvalException.class)
    public void factorial_05() {
        testError("lfn:factorial(-1)");
    }

    @Test(expected = ExprEvalException.class)
    public void factorial_06() {
        testError("lfn:factorial(5.4)");
    }

    @Test
    public void log_01() {
        test("lfn:log(1)", "0");
    }

    @Test
    public void log_02() {
        test("lfn:log(10)", "1");
    }

    @Test
    public void log_03() {
        test("lfn:log(-1)", NodeFactoryExtra.doubleToNode(Double.NaN));
    }

    @Test
    public void log_04() {
        test("lfn:log(4, 2)", "2");
    }

    @Test
    public void log_05() {
        test("lfn:log(4, 16)", "0.5");
    }

    @Test
    public void log_06() {
        test("lfn:log(16, 4)", "2");
    }

    @Test
    public void reciprocal_01() {
        test("lfn:reciprocal(1)", "1");
    }

    @Test
    public void reciprocal_02() {
        test("lfn:reciprocal(2)", "0.5");
    }

    @Test
    public void reciprocal_03() {
        test("lfn:reciprocal(lfn:reciprocal(2))", "2");
    }

    @Test
    public void root_01() {
        test("lfn:root(4,2)", "2");
    }

    @Test
    public void root_02() {
        test("lfn:root(2,1)", "2");
    }

    @Test
    public void root_03() {
        testDouble("lfn:root(64,3)", "4", DELTA);
    }

    @Test
    public void sqrt_01() {
        test("lfn:sqrt(4)", "2");
    }

    @Test
    public void sqrt_02() {
        test("lfn:sqrt(144)", "12");
    }

    @Test
    public void cartesian_01() {
        test("lfn:cartesian(0, 0, 0, 0)", "0");
    }

    @Test
    public void cartesian_02() {
        test("lfn:cartesian(0, 0, 3, 4)", "5");
    }

    @Test
    public void cartesian_03() {
        test("lfn:cartesian(0, 0, 0, 3, 4, 0)", "5");
    }

    @Test
    public void cartesian_04() {
        test("lfn:cartesian(0, 0, 0, 0, 3, 4)", "5");
    }

    @Test
    public void cartesian_05() {
        test("lfn:cartesian(0, 0, 0, 3, 0, 4)", "5");
    }

    @Test
    public void cos_01() {
        testDouble("lfn:cos(lfn:degrees-to-radians(60))", "0.5", DELTA);
    }
    
    @Test
    public void acos_01() {
        testDouble("lfn:radians-to-degrees(lfn:cos-1(lfn:cos(lfn:degrees-to-radians(60))))", "60", DELTA);
    }

    private static void test(String exprString, String result) {
        Node r = NodeFactoryExtra.parseNode(result);
        test(exprString, r);
    }

    private static void test(String exprString, Node result) {
        Expr expr = ExprUtils.parse(exprString, pmap);
        NodeValue actual = expr.eval(null, new FunctionEnvBase());
        NodeValue expected = NodeValue.makeNode(result);

        // Note that we don't test lexical form because we can get mismatches
        // between how things like doubles are expressed
        assertTrue("Not same value: Expected = " + expected + " : Actual = " + actual,
                NodeValue.sameAs(expected, actual));
    }

    private static void testDouble(String exprString, String result, double delta) {
        Node r = NodeFactoryExtra.parseNode(result);
        testDouble(exprString, r, delta);
    }

    private static void testDouble(String exprString, Node result, double delta) {
        Expr expr = ExprUtils.parse(exprString, pmap);
        NodeValue actual = expr.eval(null, new FunctionEnvBase());
        NodeValue expected = NodeValue.makeNode(result);

        // Note that we don't test lexical form because we can get mismatches
        // between how things like doubles are expressed
        if (NodeValue.sameAs(expected, actual))
            return;

        // Because Java floating point calculations are woefully imprecise we
        // are in many cases simply testing that the differences between the
        // values are within a given delta
        double difference = Math.abs(actual.getDouble() - expected.getDouble());
        assertTrue("Values not within given delta " + delta + ": Expected = " + expected + " : Actual = " + actual,
                difference <= delta);
    }

    private static void testError(String exprString) {
        Expr expr = ExprUtils.parse(exprString, pmap);
        expr.eval(null, new FunctionEnvBase());
    }

}
