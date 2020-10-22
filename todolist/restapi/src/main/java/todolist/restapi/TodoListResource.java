package todolist.restapi;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todolist.core.AbstractTodoList;
import todolist.core.TodoModel;

/**
 * Used for all requests referring to TodoLists by name.
 */
public class TodoListResource {

  private static final Logger LOG = LoggerFactory.getLogger(TodoListResource.class);

  private final TodoModel todoModel;
  private final String name;
  private final AbstractTodoList todoList;

  /**
   * Initializes this TodoListResource with appropriate context information.
   * Each method will check and use what it needs.
   *
   * @param todoModel the TodoModel, needed for DELETE and rename
   * @param name the todo list name, needed for most requests
   * @param todoList the TodoList, or null, needed for PUT
   */
  public TodoListResource(TodoModel todoModel, String name, AbstractTodoList todoList) {
    this.todoModel = todoModel;
    this.name = name;
    this.todoList = todoList;
  }

  private void checkTodoList() {
    if (this.todoList == null) {
      throw new IllegalArgumentException("No TodoList named \"" + name + "\"");
    }
  }

  /**
   * Gets the corresponding TodoList.
   *
   * @return the corresponding TodoList
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public AbstractTodoList getTodoList() {
    checkTodoList();
    LOG.debug("getTodoList({})", name);
    return this.todoList;
  }

  /**
   * Replaces or adds a TodoList.
   *
   * @param todoListArg the todoList to add
   * @return true if it was added, false if it replaced
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public boolean putTodoList(AbstractTodoList todoListArg) {
    LOG.debug("putTodoList({})", todoListArg);
    return this.todoModel.putTodoList(todoListArg) == null;
  }

  /**
   * Adds a TodoList with the given name, if it doesn't exist already.
   */
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  public boolean putTodoList() {
    return putTodoList(null);
  }

  /**
   * Renames the TodoList.
   *
   * @param newName the newName
   */
  @POST
  @Path("/rename")
  @Produces(MediaType.APPLICATION_JSON)
  public boolean renameTodoList(@QueryParam("newName") String newName) {
    checkTodoList();
    if (this.todoModel.getTodoList(newName) != null) {
      throw new IllegalArgumentException("A TodoList named \"" + newName + "\" already exists");
    }
    this.todoList.setName(newName);
    return true;
  }

  /**
   * Removes the TodoList.
   */
  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  public boolean removeTodoList() {
    checkTodoList();
    this.todoModel.removeTodoList(this.todoList);
    return true;
  }
}