package main.java;

import com.fasterxml.jackson.databind.*;

import java.io.*;

/**
 * Class containing two methods. One to serialize objects, and one to deserialize objects
 */
public class Json {

    /*
    Methods adapted from:
    https://www.youtube.com/watch?v=Hv_a3ZBSO_g
    https://www.youtube.com/watch?v=zqoYDkDl2Z4
     */

    private final static ObjectMapper OM = getDefault();
    private final static ObjectWriter OW = OM.writer().with(SerializationFeature.INDENT_OUTPUT);

    private static ObjectMapper getDefault() {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }

    private static String readJson(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String res = "";
        String line;
        while ((line = reader.readLine()) != null) {
            res += line;
        }

        reader.close();
        return res;
    }

    private static JsonNode parseJson(String json) throws IOException {
        return OM.readTree(json);
    }

    public static <A> A load(String file, Class<A> clazz) throws IOException {
        String json = readJson(file);
        return OM.treeToValue(parseJson(json), clazz);
    }

    private static JsonNode deflate(Object o) {
        return OM.valueToTree(o);
    }

    private static String nodeToString(JsonNode node) throws IOException {
        return OW.writeValueAsString(node);
    }

    public static void dump(Object o, String file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(nodeToString(deflate(o)));
        writer.close();
    }
}
