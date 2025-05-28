package dev.hnnguyen.blog.repository.impl;

import dev.hnnguyen.blog.domain.dto.common.RequestSqlCondition;
import dev.hnnguyen.blog.domain.enums.ConditionTypeEnum;
import dev.hnnguyen.blog.domain.enums.FilterTypeEnum;
import dev.hnnguyen.blog.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.BiConsumer;

public class SqlQueryBuilder {

    public static String generateSqlQuery(List<RequestSqlCondition> conditions) {
        StringJoiner whereClause = new StringJoiner(" ");
        buildConditions(whereClause, conditions);
        return whereClause.toString();
    }

    private static void buildConditions(StringJoiner whereClause, List<RequestSqlCondition> conditions) {
        BiConsumer<StringJoiner, RequestSqlCondition> appendCondition = (clause, condition) -> {
            if (condition.isGroup()) {
                StringJoiner subClause = new StringJoiner(" ");
                buildConditions(subClause, condition.getSubConditions());
                if (subClause.length() > 0) {
                    clause.add("(" + subClause + ")");
                }
            } else {
                var value = condition.getValue();
                if (FilterTypeEnum.DATE.equals(condition.getFilterType())) {
                    if (Objects.nonNull(condition.getTimezone())) {
                        condition.setColumn("TO_CHAR(" + condition.getColumn() + " AT TIME ZONE 'UTC' AT TIME ZONE '" + condition.getTimezone() + "', 'YYYY-MM-DD')");
                    } else {
                        condition.setColumn("TO_CHAR(" + condition.getColumn() + ", 'YYYY-MM-DD')");
                    }
                    value = String.format("'%s'", value);
                } else if (FilterTypeEnum.NUMBER.equals(condition.getFilterType())) {
                    value = new BigDecimal(String.valueOf(value));
                } else {
                    value = String.format("'%s'", value);
                }

                switch (condition.getConditionType()) {
                    case EQUAL, DATE_IS ->
                        clause.add(String.format(ConditionTypeEnum.EQUAL.getOperator(), condition.getColumn(), value));
                    case CONTAIN -> {
                        clause.add(String.format(ConditionTypeEnum.CONTAIN.getOperator(), condition.getColumn(), "'%" + StringUtils.wildcards(condition.getValue().toString()) + "%'"));
                        clause.add(" ESCAPE '\\' ");
                    }
                    case ENDS_WITH ->
                        clause.add(String.format(ConditionTypeEnum.ENDS_WITH.getOperator(), condition.getColumn(), "'%" + condition.getValue() + "'"));
                    case BEGINS_WITH ->
                        clause.add(String.format(ConditionTypeEnum.BEGINS_WITH.getOperator(), condition.getColumn(), "'" + condition.getValue() + "%'"));
                    case DOES_NOT_CONTAIN, DATE_IS_NOT ->
                        clause.add(String.format(ConditionTypeEnum.DOES_NOT_CONTAIN.getOperator(), condition.getColumn(), "'%" + condition.getValue() + "%'"));
                    case DOES_NOT_EQUAL ->
                        clause.add(String.format(ConditionTypeEnum.DOES_NOT_EQUAL.getOperator(), condition.getColumn(), value));
                    case BLANK ->
                        clause.add(String.format(ConditionTypeEnum.BLANK.getOperator(), condition.getColumn()));
                    case NOT_BLANK ->
                        clause.add(String.format(ConditionTypeEnum.NOT_BLANK.getOperator(), condition.getColumn()));
                    case BEFORE, DATE_BEFORE, LESS_THAN ->
                        clause.add(String.format(ConditionTypeEnum.BEFORE.getOperator(), condition.getColumn(), value));
                    case LESS_THAN_OR_EQUAL ->
                        clause.add(String.format(ConditionTypeEnum.LESS_THAN_OR_EQUAL.getOperator(), condition.getColumn(), value));
                    case AFTER, DATE_AFTER, GREATER_THAN ->
                        clause.add(String.format(ConditionTypeEnum.AFTER.getOperator(), condition.getColumn(), value));
                    case GREATER_THAN_OR_EQUAL ->
                        clause.add(String.format(ConditionTypeEnum.GREATER_THAN_OR_EQUAL.getOperator(), condition.getColumn(), value));
                    case BETWEEN -> {
                        if (condition.getValues() != null && condition.getValues().size() == 2) {
                            clause.add(String.format(ConditionTypeEnum.GREATER_THAN_OR_EQUAL.getOperator(), condition.getColumn(),
                                condition.getValues().get(0), condition.getValues().get(1)));
                        }
                    }
                }
            }
        };

        for (int i = 0; i < conditions.size(); i++) {
            RequestSqlCondition condition = conditions.get(i);
            if (!condition.isGroup() && condition.getValue() == null &&
                (condition.getConditionType() != ConditionTypeEnum.BLANK && condition.getConditionType() != ConditionTypeEnum.NOT_BLANK)) {
                continue;
            }
            if (i > 0 && condition.getCombinationType() != null) {
                whereClause.add(condition.getCombinationType().toString());
            }
            appendCondition.accept(whereClause, condition);
        }
    }
}
