package com.github.xml;

import com.github.xml.pojo.Address;
import com.github.xml.pojo.Customer;
import com.github.xml.pojo.Order;
import com.github.xml.pojo.Shipping;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class XSDFactory {

    public static void main(String... args) throws JAXBException, IOException {
        XSDFactory xsdCreate = new XSDFactory();
        CustomSchemaOutputResolver resolver = xsdCreate.new CustomSchemaOutputResolver(
                "D:\\IdeaSpace\\jdepth\\src\\main\\java\\com\\github\\xml",
                "order.xsd"
        );

        Class[] classes = {Order.class, Address.class, Customer.class, Shipping.class};
        JAXBContext context = JAXBContext.newInstance(classes);
        context.generateSchema(resolver);
    }

    public class CustomSchemaOutputResolver extends SchemaOutputResolver {

        private File file;

        public CustomSchemaOutputResolver(String dir, String fileName) {
            try {
                file = new File(dir, fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
            return new StreamResult(file);
        }

    }
}
