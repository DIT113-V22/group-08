package util;

import com.fasterxml.jackson.databind.*;

import java.io.*;

/**
 * Class containing two methods. One to serialize objects, and one to deserialize objects
 */
public class Json {

    private final static ObjectMapper OM = getDefault();
    private final static ObjectWriter OW = OM.writer().with(SerializationFeature.INDENT_OUTPUT);

    /**
     * Generates a default {@link ObjectMapper} that does not fail when receiving unknown properties
     *
     * @return an {@link ObjectMapper}
     */
    private static ObjectMapper getDefault() {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }

    /**
     * Reads from a file and returns a string with all the file's contents
     *
     * @param file file to read from
     * @return a string with all the text from the file
     * @throws IOException if the file does not exist
     */
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

    /**
     * Generates a {@link JsonNode} from the provided string
     *
     * @param json source string
     * @return a {@link JsonNode} representing the source's contents
     * @throws IOException
     */
    private static JsonNode parseJson(String json) throws IOException {
        return OM.readTree(json);
    }

    /**
     * Deserializes an object from JSON.
     * Attribute names must match key names in the JSON file.
     * Attributes must be public or have setters.
     *
     * @param file absolute file path.
     *             Example: {@code getClass().getResources(<file name>).getFile()}
     * @param clazz class to create an instance of
     * @param <A> any class to deserialize, must have a default constructor
     * @return an instance of the provided class
     * @throws IOException if JSON loading fails, e.g. file does not exist
     */
    public static <A> A load(String file, Class<A> clazz) throws IOException {
        String json = readJson(file);
        return OM.treeToValue(parseJson(json), clazz);
    }

    /**
     * Deflates an object to a {@link JsonNode}
     *
     * @param o object to serialize to JSON
     * @return a JsonNode representing the object
     */
    private static JsonNode deflate(Object o) {
        return OM.valueToTree(o);
    }

    /**
     * Generates a string representation of a {@code JsonNode}
     *
     * @param node {@code JsonNode} to generate a string representation of
     * @return a string to be written to a JSON file
     * @throws IOException
     */
    private static String nodeToString(JsonNode node) throws IOException {
        return OW.writeValueAsString(node);
    }

    /**
     * Serializes an object to JSON.
     *
     * @param o object to serialize to JSON
     * @param file destination of the serialized object
     * @throws IOException if the target path is a directory
     */
    public static void dump(Object o, String file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(nodeToString(deflate(o)));
        writer.close();
    }
}

