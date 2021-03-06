/*
 * Copyright (c)  RWTH Aachen. All rights reserved.
 *
 * http://www.se-rwth.de/
 */
package org.nest.nestml._symboltable.typechecking;

import org.nest.nestml._symboltable.symbols.TypeSymbol;
import org.nest.nestml._symboltable.unitrepresentation.UnitRepresentation;

import static org.nest.nestml._symboltable.predefined.PredefinedTypes.*;
import static org.nest.nestml._symboltable.symbols.TypeSymbol.Type.UNIT;

/**
 * Helper routine to calculate the category of the particular type.
 *
 * @author plotnikov
 */
public class TypeChecker {
  public static boolean  isCompatible(final String lhsType, final String rhsType) {
    return isCompatible(getType(lhsType), getType(rhsType));
  }

  public static boolean  isCompatible(final TypeSymbol lhsType, final TypeSymbol rhsType) {

    //simplified check for Units set to ignore magnitude: (ignore if any is set)
    if(lhsType.getType().equals(UNIT) && rhsType.getType().equals(UNIT)){
      UnitRepresentation lhsUnit = UnitRepresentation.getBuilder().serialization(lhsType.getName()).build();
      UnitRepresentation rhsUnit = UnitRepresentation.getBuilder().serialization(rhsType.getName()).build();
      return lhsUnit.equals(rhsUnit);
    }

    if (lhsType.equals(rhsType)) {
      return true;
    }
    if (lhsType.equals(getRealType()) &&
        rhsType.equals(getIntegerType())) {
      return true;
    }

    return false;
  }
  /**
   * Checks that the {@code type} is a numeric type {@code Integer} or {@code Real}.
   */
  public boolean checkNumber(final TypeSymbol type) {
    return checkInteger(type) || checkReal(type);
  }

  /**
   * Checks that the {@code type} is an {@code Integer}.
   */
  private boolean checkInteger(final TypeSymbol u) {
    return u != null && u.getName().equals(getIntegerType().getName());
  }

  /**
   * Checks that the {@code type} is an {@code real}.
   */
  private boolean checkReal(final TypeSymbol u) {
    return u != null && u.getName().equals(getRealType().getName());
  }

  public static boolean isVoid(final TypeSymbol type) {
    return type != null && type.getName().equals(getVoidType().getName());
  }

  public static boolean isString(final TypeSymbol type) {
    return type != null && type.getName().equals(getStringType().getName());

  }

  public static boolean isBoolean(final TypeSymbol type) {
    // TODO use prover equals implementation
    return type != null && type.getName().equals(getBooleanType().getName());
  }

  public static boolean isUnit(final TypeSymbol rType) {
    //return rType.getName().equals(getUnitType().getName()); // TODO use prover equals implementation
    return rType != null && rType.getType().equals(UNIT);
  }

  public static boolean isInteger(TypeSymbol typeSymbol) {
    // TODO use prover equals implementation
    return typeSymbol != null && typeSymbol.equals(getIntegerType());
  }

  public static boolean isReal(TypeSymbol typeSymbol) {
    // TODO use prover equals implementation
    return typeSymbol != null && typeSymbol.equals(getRealType());
  }

  public static boolean isNumericPrimitive(TypeSymbol typeSymbol) {
    // TODO use prover equals implementation
    return typeSymbol != null && (typeSymbol.equals(getRealType()) ||
        typeSymbol.equals(getIntegerType()));
  }

  public static boolean isPrimitiveTypeName(String typeName){
    return typeName != null && (
            typeName.equals(getVoidType().getName()) ||
            typeName.equals(getStringType().getName()) ||
            typeName.equals(getBooleanType().getName()) ||
            typeName.equals(getIntegerType().getName()) ||
            typeName.equals(getRealType().getName()) );
  }

  public static String deserializeUnitIfNotPrimitive(String typeName){
    if(isPrimitiveTypeName(typeName)){
      return typeName;
    }else{
      return UnitRepresentation.getBuilder().serialization(typeName).build().prettyPrint();
    }
  }

  public static boolean isNumeric(final TypeSymbol type) {
    return type != null && (type.equals(getIntegerType()) ||
        type.equals(getRealType()) ||
        type.getType().equals(TypeSymbol.Type.UNIT));
  }


}
