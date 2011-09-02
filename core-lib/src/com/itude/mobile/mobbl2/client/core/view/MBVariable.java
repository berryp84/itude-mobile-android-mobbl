package com.itude.mobile.mobbl2.client.core.view;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBForEachDefinition;
import com.itude.mobile.mobbl2.client.core.configuration.mvc.MBVariableDefinition;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;
import com.itude.mobile.mobbl2.client.core.util.StringUtilities;

public class MBVariable extends MBComponentContainer
{
  private String _expression;
  private String _name;

  public MBVariable(MBDefinition definition, MBDocument document, MBComponentContainer parent)
  {
    super(definition, document, parent);

    MBVariableDefinition varDef = (MBVariableDefinition) getDefinition();

    setName(substituteExpressions(varDef.getName()));
    setExpression(substituteExpressions(varDef.getExpression()));

    MBForEachDefinition eachDef = (MBForEachDefinition) getParent().getParent().getDefinition();
    eachDef.addVariable(varDef);

  }

  public void setName(String name)
  {
    this._name = name;
  }

  public String getName()
  {
    return _name;
  }

  public void setExpression(String expression)
  {
    this._expression = expression;
  }

  public String getExpression()
  {
    return _expression;
  }

  @Override
  public StringBuffer asXmlWithLevel(StringBuffer appendToMe, int level)
  {
    StringUtilities.appendIndentString(appendToMe, level)
                  .append("<MBVariable ")
                  .append(attributeAsXml("name", _name))
                  .append(" ")
                  .append(attributeAsXml("expression", _expression))
                  .append(">\n");

    childrenAsXmlWithLevel(appendToMe, level + 2);
    
    return StringUtilities.appendIndentString(appendToMe, level)
                        .append("</MBVariable>\n");
  }

  @Override
  public String toString()
  {
    StringBuffer rt = new StringBuffer();
    return asXmlWithLevel(rt, 0).toString();
  }

}
