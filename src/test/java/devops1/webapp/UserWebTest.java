package devops1.webapp;

import net.sourceforge.jwebunit.html.Row;
import net.sourceforge.jwebunit.html.Table;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ResourceBundle;

import static net.sourceforge.jwebunit.junit.JWebUnit.*;

public class UserWebTest {
    private ResourceBundle messages;

    @Before
    public void setUp() {
        setScriptingEnabled(false);
        getTestContext().setBaseUrl(
            "http://" + System.getProperty("cargo.host") + ":" + System.getProperty("cargo.port"));
        getTestContext().setResourceBundleName("messages");
        messages = ResourceBundle.getBundle("messages");
    }

    @Before
    public void addUser() {
        beginAt("/userform");
        assertTitleKeyMatches("userForm.title");
        setTextField("username", "springuser");
        setTextField("password", "springuser");
        setTextField("firstName", "Spring");
        setTextField("lastName", "User");
        setTextField("email", "springuser@appfuse.org");
        clickButton("save");
    }

    @Test
    public void welcomePage() {
        beginAt("/");
        assertTitleKeyMatches("index.title");
    }

    @Test
    public void listUsers() {
        beginAt("/users");
        assertTitleKeyMatches("userList.title");

        // check that table is present
        assertTablePresent("userList");

        //check that a set of strings are present somewhere in table
        assertTextInTable("userList", new String[]{"Spring", "User"});
    }

    @Test
    public void editUser() {
        beginAt("/userform?id=" + getInsertedUserId());
        assertTextFieldEquals("firstName", "Spring");
        clickButton("save");
        assertTitleKeyMatches("userList.title");
    }

    @After
    public void removeUser() {
        beginAt("/userform?id=" + getInsertedUserId());
        assertTitleKeyMatches("userForm.title");
        clickButton("delete");
        assertTitleKeyMatches("userList.title");
    }

    /**
     * Convenience method to get the id of the inserted user
     * Assumes last inserted user is "Spring User"
     *
     * @return last id in the table
     */
    protected String getInsertedUserId() {
        beginAt("/users");
        assertTablePresent("userList");
        assertTextInTable("userList", "Spring");
        Table table = getTable("userList");
        // Find row with Spring in it, skip header row
        for (int i = 1; i < table.getRows().size(); i++) {
            Row row = table.getRows().get(i);
            if (i == table.getRowCount() - 1) {
                return row.getCells().get(0).getValue();
            }
        }
        return "";
    }

    private void assertTitleKeyMatches(String title) {
        assertTitleEquals(messages.getString(title) + " | " + messages.getString("webapp.name"));
    }
}
