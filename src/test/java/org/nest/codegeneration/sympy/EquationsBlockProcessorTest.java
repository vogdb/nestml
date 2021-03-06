/*
 * EquationsBlockProcessorTest.java
 *
 * This file is part of NEST.
 *
 * Copyright (C) 2004 The NEST Initiative
 *
 * NEST is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * NEST is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NEST.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.nest.codegeneration.sympy;

import de.monticore.symboltable.Scope;
import de.se_rwth.commons.Names;
import org.junit.Ignore;
import org.junit.Test;
import org.nest.base.ModelbasedTest;
import org.nest.codegeneration.sympy.EquationsBlockProcessor;
import org.nest.nestml._ast.ASTNESTMLCompilationUnit;
import org.nest.nestml._ast.ASTNeuron;
import org.nest.nestml._symboltable.symbols.NeuronSymbol;
import org.nest.nestml._symboltable.symbols.VariableSymbol;
import org.nest.nestml.prettyprinter.NESTMLPrettyPrinter;
import org.nest.utils.AstUtils;
import org.nest.utils.FilesHelper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

/**
 * Tests if the overall transformation solveOdeWithShapes works
 *
 * @author plotnikov
 */
public class EquationsBlockProcessorTest extends ModelbasedTest {
  private static final String COND_MODEL_FILE = "models/iaf_cond_alpha.nestml";
  private static final String PSC_MODEL_FILE = "models/iaf_neuron.nestml";
  private static final String PSC_DELTA_MODEL_FILE = "models/iaf_psc_delta.nestml";

  private final EquationsBlockProcessor testant = new EquationsBlockProcessor();

  @Test
  public void test_psc_model() throws Exception {
    final Scope scope = solveOdesAndShapes(PSC_MODEL_FILE);

    final Optional<NeuronSymbol> neuronSymbol = scope.resolve("iaf_neuron_nestml", NeuronSymbol.KIND);

    final Optional<VariableSymbol> y1 = neuronSymbol.get().getVariableByName("G");
    final Optional<VariableSymbol> y2 = neuronSymbol.get().getVariableByName("G__1");
    assertTrue(y1.isPresent());
    assertTrue(y1.get().getBlockType().equals(VariableSymbol.BlockType.STATE));

    assertTrue(y2.isPresent());
    assertTrue(y2.get().getBlockType().equals(VariableSymbol.BlockType.STATE));
  }

  @Test
  public void test_cond_model() throws Exception {
    final Scope scope = solveOdesAndShapes(COND_MODEL_FILE);
    final Optional<NeuronSymbol> neuronSymbol = scope.resolve("iaf_cond_alpha_neuron", NeuronSymbol.KIND);
    final Optional<VariableSymbol> y1 = neuronSymbol.get().getVariableByName("g_in");
    final Optional<VariableSymbol> y2 = neuronSymbol.get().getVariableByName("g_ex");
    assertTrue(y1.isPresent());
    assertTrue(y1.get().getBlockType().equals(VariableSymbol.BlockType.INITIAL_VALUES));

    assertTrue(y2.isPresent());
    assertTrue(y2.get().getBlockType().equals(VariableSymbol.BlockType.INITIAL_VALUES));
  }

  @Test
  public void test_delta_model() throws Exception {
    final Scope scope =  solveOdesAndShapes(PSC_DELTA_MODEL_FILE);
    final Optional<NeuronSymbol> neuronSymbol = scope.resolve("iaf_psc_delta_neuron", NeuronSymbol.KIND);
    final Optional<VariableSymbol> var = neuronSymbol.get().getVariableByName("__ode_var_factor");
    assertTrue(var.isPresent());
    assertTrue(var.get().getBlockType().equals(VariableSymbol.BlockType.INTERNALS));

  }

  /**
   * Parses model, builds symboltable, cleanups output folder by deleting tmp file and processes ODEs from model.i
   */
  private Scope solveOdesAndShapes(final String pathToModel) {
    final ASTNESTMLCompilationUnit modelRoot = parseNestmlModel(pathToModel);
    scopeCreator.runSymbolTableCreator(modelRoot);
    FilesHelper.deleteFilesInFolder(OUTPUT_FOLDER);

    final ASTNeuron solvedNeuron = testant.solveOdeWithShapes(modelRoot.getNeurons().get(0), OUTPUT_FOLDER);

    return AstUtils.deepCloneNeuronAndBuildSymbolTable(solvedNeuron, OUTPUT_FOLDER).getSpannedScope().get();

  }

}