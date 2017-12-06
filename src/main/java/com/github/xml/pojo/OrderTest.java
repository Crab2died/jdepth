package com.github.xml.pojo;

import org.jibx.runtime.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

public class OrderTest {

    private IBindingFactory factory = null;

    private String encode2Xml(Order order) throws JiBXException, IOException {
        factory = BindingDirectory.getFactory(Order.class);
        StringWriter writer = new StringWriter();
        IMarshallingContext mc = factory.createMarshallingContext();
        mc.setIndent(2);
        mc.marshalDocument(order, "UTF-8", null, writer);
        String xml = writer.toString();
        writer.close();
        return xml;
    }

    private Order decode2Object(String xml) throws JiBXException {
        StringReader reader = new StringReader(xml);
        IUnmarshallingContext umc = factory.createUnmarshallingContext();
        return (Order) umc.unmarshalDocument(reader);
    }

    public static void main(String... args) {
        OrderTest test = new OrderTest();
        Address address = new Address("1号路", null, "OAA", "121", "3456789", "Okays");
        Customer customer = new Customer(123456, "杨", "UKU", new ArrayList<String>(){{
            add("ASD");
            add("SYM");
        }});
        Order order = new Order(654321, customer, address, Shipping.DOMESTIC_EXPRESS, address, 514.1F);
        try {
           String xml = test.encode2Xml(order);
           Order obj = test.decode2Object(xml);
           System.out.println(xml);
           System.out.println(obj);
        } catch (JiBXException | IOException e) {
            e.printStackTrace();
        }
    }
}
