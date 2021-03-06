package com.github.cschulc.jirarestservice.jql;

public enum KeywordEnum {

    /**
     * Used to combine multiple clauses, allowing you to refine your search.
     * <p>Examples:
     * <li>project = "New office" and status = "open"
     * <li>status = open and priority = urgent and assignee = jsmith
     * <li>project = JRA and assignee != jsmith
     * <li>project in (JRA,CONF) and fixVersion = "3.14"
     * <li>reporter not in (Jack,Jill,John) and assignee not in (Jack,Jill,John)
     */
    AND("and"),

    /**
     * Used to combine multiple clauses, allowing you to expand your search.
     * <p>Examples:
     * <li>reporter = jsmith or reporter = jbrown
     * <li>duedate < now() or duedate is empty
     */
    OR("or"),

    /**
     * Used to negate individual clauses or a complex JQL query (a query made up of more than one clause)
     * using parentheses, allowing you to refine your search.
     * <p>Examples:
     * <li>not assignee = jsmith
     * <li>not (reporter = jsmith or reporter = jbrown)
     */
    NOT("not"),

    /**
     * Used to search for issues where a given field does not have a value.
     * <p>Examples:
     * <li>duedate = empty
     * <li>duedate is empty
     */
    EMPTY("empty"),

    /**
     * Used to search for issues where a given field does not have a value.
     * <p>Examples:
     * <li>duedate = null
     * <li>duedate is null
     */
    NULL("null"),

    /**
     * Used to specify the fields by whose values the search results will be sorted.
     * <p>Examples:
     * <li>duedate = empty order by created
     * <li>duedate = empty order by created, priority desc
     * <li>duedate = empty order by created, priority asc
     */
    ORDER_BY("order by");

    /**
     * The keyword.
     */
    private final String keyword;

    /**
     * Instantiates a new keyword.
     *
     * @param keyword name of the keyword
     */
    KeywordEnum(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Gets the keyword.
     *
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    @Override
    public String toString() {
        return getKeyword();
    }
}
