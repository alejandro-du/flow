package com.vaadin.hummingbird.kernel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.vaadin.hummingbird.kernel.ValueType.ArrayType;
import com.vaadin.hummingbird.kernel.ValueType.ObjectType;

public class ValueTypeTest {

    private ObjectType simpleObjectType = ValueType
            .get(Collections.singletonMap("a", ValueType.STRING));

    private ArrayType simpleArrayType = ValueType
            .get(simpleObjectType.getPropertyTypes(), simpleObjectType);

    @Test
    public void testPredefinedIds() {
        Assert.assertEquals("Id is not sync with client code", 0,
                ValueType.STRING.getId());
        Assert.assertEquals("Id is not sync with client code", 1,
                ValueType.BOOLEAN.getId());
        Assert.assertEquals("Id is not sync with client code", 2,
                ValueType.INTEGER.getId());
        Assert.assertEquals("Id is not sync with client code", 3,
                ValueType.NUMBER.getId());
    }

    @Test
    public void testObjectTypeIdentity() {
        Assert.assertSame(simpleObjectType,
                ValueType.get(Collections.singletonMap("a", ValueType.STRING)));

        Assert.assertNotSame(simpleObjectType,
                ValueType.get(Collections.singletonMap("b", ValueType.STRING)));
        Assert.assertNotSame(simpleObjectType, ValueType
                .get(Collections.singletonMap("a", ValueType.BOOLEAN)));
    }

    @Test
    public void testObjectTypeDefensiveCopy() {
        Map<Object, ValueType> propertyTypes = new HashMap<>();
        propertyTypes.put("unique", ValueType.STRING);
        ObjectType valueType = ValueType.get(propertyTypes);

        Assert.assertEquals(propertyTypes, valueType.getPropertyTypes());

        propertyTypes.put("b", ValueType.INTEGER);

        Assert.assertNotEquals(propertyTypes, valueType.getPropertyTypes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testObjectTypeImmutable() {
        simpleObjectType.getPropertyTypes().put("b", ValueType.BOOLEAN);
    }

    @Test
    public void testArrayTypeIdentity() {
        Assert.assertSame(simpleArrayType, ValueType
                .get(simpleObjectType.getPropertyTypes(), simpleObjectType));

        Assert.assertNotSame(simpleArrayType, ValueType
                .get(Collections.singletonMap("a", ValueType.STRING), ValueType
                        .get(Collections.singletonMap("b", ValueType.STRING))));
        Assert.assertNotSame(simpleObjectType,
                ValueType.get(Collections.singletonMap("b", ValueType.STRING),
                        simpleObjectType));
        Assert.assertNotSame(simpleObjectType,
                ValueType.get(Collections.singletonMap("a", ValueType.BOOLEAN),
                        simpleObjectType));
    }

    @Test
    public void testArrayTypeDefensiveCopy() {
        Map<Object, ValueType> propertyTypes = new HashMap<>();
        propertyTypes.put("unique2", ValueType.STRING);
        ObjectType valueType = ValueType.get(propertyTypes, simpleObjectType);

        Assert.assertEquals(propertyTypes, valueType.getPropertyTypes());

        propertyTypes.put("b", ValueType.INTEGER);

        Assert.assertNotEquals(propertyTypes, valueType.getPropertyTypes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testArrayTypeImmutable() {
        simpleArrayType.getPropertyTypes().put("b", ValueType.BOOLEAN);
    }
}