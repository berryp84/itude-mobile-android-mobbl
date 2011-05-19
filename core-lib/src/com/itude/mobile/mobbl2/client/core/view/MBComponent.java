package com.itude.mobile.mobbl2.client.core.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;

import com.itude.mobile.mobbl2.client.core.configuration.MBDefinition;
import com.itude.mobile.mobbl2.client.core.controller.MBOutcome;
import com.itude.mobile.mobbl2.client.core.controller.MBViewManager;
import com.itude.mobile.mobbl2.client.core.model.MBDocument;

public class MBComponent
{
  private MBDefinition         _definition;
  private MBComponentContainer _parent;
  private String               _style;
  private Map<String, Object>  _viewData;
  private boolean              _markedForDestruction;
  private int                  _leftInset;
  private int                  _rightInset;
  private int                  _topInset;
  private int                  _bottomInset;
  private MBDocument           _document;

  public MBComponent(MBDefinition definition, MBDocument document, MBComponentContainer parent)
  {
    _definition = definition;
    _document = document;
    _parent = parent;

    // Not all definitions have a style attribute; if they do set it
    if (MBStylableDefinition.class.isAssignableFrom(definition.getClass()))
    {
      setStyle(((MBStylableDefinition) definition).getStyle());
    }

    _viewData = null;
  }

  public MBDefinition getDefinition()
  {
    return _definition;
  }

  public void setDefinition(MBDefinition definition)
  {
    _definition = definition;
  }

  public void setDocument(MBDocument document)
  {
    _document = document;
  }

  public MBComponentContainer getParent()
  {
    return _parent;
  }

  public void setParent(MBComponentContainer parent)
  {
    _parent = parent;
  }

  public String getStyle()
  {
    return _style;
  }

  public void setStyle(String style)
  {
    _style = style;
  }

  public boolean isMarkedForDestruction()
  {
    return _markedForDestruction;
  }

  public void setMarkedForDestruction(boolean markedForDestruction)
  {
    _markedForDestruction = markedForDestruction;
  }

  public int getLeftInset()
  {
    return _leftInset;
  }

  public void setLeftInset(int leftInset)
  {
    _leftInset = leftInset;
  }

  public int getRightInset()
  {
    return _rightInset;
  }

  public void setRightInset(int rightInset)
  {
    _rightInset = rightInset;
  }

  public int getTopInset()
  {
    return _topInset;
  }

  public void setTopInset(int topInset)
  {
    _topInset = topInset;
  }

  public int getBottomInset()
  {
    return _bottomInset;
  }

  public void setBottomInset(int bottomInset)
  {
    _bottomInset = bottomInset;
  }

  public View buildViewWithMaxBounds(MBViewManager.MBViewState viewState)
  {
    return null;
  }

  public void handleOutcome(MBOutcome outcome)
  {
    _parent.handleOutcome(outcome);
  }

  public void handleOutcome(String outcomeName, String path)
  {
    _parent.handleOutcome(outcomeName, path);
  }

  public String substituteExpressions(String expression)
  {
    if (expression == null || expression.indexOf('{') < 0) return expression;

    StringBuilder result = new StringBuilder();; // we really want to end this statement

    int positionOf$ = 0;
    int previousPositionOf$ = 0;

    boolean evalToNil = false;
    while ((positionOf$ = expression.indexOf("${", previousPositionOf$)) > -1)
    {
      // get everything before the ${
      result.append(expression.substring(previousPositionOf$, positionOf$));
      // for the next while-iteration, make sure we skip the ${ we are now processing
      previousPositionOf$ = positionOf$ + 2;

      int endingAccolade;
      if ((endingAccolade = expression.indexOf('}', positionOf$ + 2)) != -1)
      {
        previousPositionOf$ = endingAccolade + 1;
        String theExpressionFound = expression.substring(positionOf$ + 2, endingAccolade);

        String resultOfTheExpresion = evaluateExpression(theExpressionFound);
        if (resultOfTheExpresion != null)
        {
          result.append(resultOfTheExpresion);
        }
        else
        {
          evalToNil = true;
        }
      }
    }

    // get the remainder of the original expression
    result.append(expression.substring(previousPositionOf$));

    if (result.length() == 0 && evalToNil) return null;
    else return result.toString();
  }

  public String getComponentDataPath()
  {
    return "";
  }

  public String getAbsoluteDataPath()
  {
    String componentPath = getComponentDataPath();

    // If the path is not set (a field without a path specified for instance) return nil; since it then also does not have an absolute path:
    if (componentPath == null)
    {
      return null;
    }

    // Absolute path set for the component? (possibly using a doc:path expression) Than do not prefix with the parent path and return it:
    if (componentPath.startsWith("/") || componentPath.contains(":"))
    {
      return componentPath;
    }

    String pathToMe = null;
    if (_parent != null) pathToMe = _parent.getAbsoluteDataPath();
    if (pathToMe != null && !pathToMe.endsWith("/"))
    {
      pathToMe = pathToMe + "/";
    }
    if (pathToMe == null)
    {
      pathToMe = "";
    }

    if (componentPath != null)
    {
      pathToMe = pathToMe + componentPath;
    }

    return pathToMe;
  }

  public MBDocument getDocument()
  {
    return _document;
  }

  public MBPage getPage()
  {
    if (_parent != null)
    {
      return _parent.getPage();
    }
    else
    {
      return null;
    }
  }

  public void setViewData(Object value, String key)
  {
    if (_viewData == null)
    {
      _viewData = new HashMap<String, Object>();
    }

    _viewData.put(key, value);
  }

  public Object getViewData(String key)
  {
    return _viewData.get(key);
  }

  public String evaluateExpression(String variableName)
  {
    if (getParent() != null)
    {
      return getParent().evaluateExpression(variableName);
    }

    Object value = getDocument().getValueForPath(variableName);
    if (value instanceof String) return (String) value;
    else if (value != null) return value.toString();

    return null;
  }

  public StringBuffer asXmlWithLevel(StringBuffer p_appendToMe, int level)
  {
    return p_appendToMe;
  }

  public String attributeAsXml(String name, Object attrValue)
  {
    return attrValue == null ? "" : name + "='" + attrValue + "'";
  }

  public String getDescription()
  {
    StringBuffer rt = new StringBuffer();
    return asXmlWithLevel(rt, 0).toString();
  }

  public void translatePath()
  {
  }

  public ArrayList<Object> getDescendantsOfKind(Class<?> clazz)
  {
    // This method is overridden by the various subclasses; if this could be an abstract method it would be
    return new ArrayList<Object>();
  }

  // TODO method has a selector, needs implementation
  public ArrayList<?> getDescendantsOfKind(Class<?> clazz, Object selector, Object value)
  {

    return null;
  }

  public ArrayList<MBComponent> getChildrenOfKind(Class<?> clazz)
  {
    // This method is overridden by the various subclasses; if this could be an abstract method it would be
    return new ArrayList<MBComponent>();
  }

  //TODO method has a selector, needs implementation
  public ArrayList<?> getChildrenOfKind(Class<?> clazz, Object selector, Object value)
  {
    return null;
  }

  // TODO filterSet Method should be implemented

  public Object getFirstDescendantOfKind(Class<?> clazz)
  {
    List<Object> result = getDescendantsOfKind(clazz);

    if (result.size() == 0)
    {
      return null;
    }

    return result.get(0);
  }

  // TODO method has a selector, needs implementation
  public Object getFirstDescendantOfKind(Class<?> clazz, Object selector, Object value)
  {
    return null;
  }

  public MBComponent getFirstChildOfKind(Class<?> clazz)
  {
    ArrayList<MBComponent> result = getChildrenOfKind(clazz);

    if (result.size() == 0)
    {
      return null;
    }

    return result.get(0);
  }

  @SuppressWarnings("unchecked")
  public <T extends MBComponent> T getFirstChildOfKindWithName(Class<?> clazz, String name)
  {
    ArrayList<MBComponent> result = getChildrenOfKind(clazz);

    for (MBComponent component : result)
    {
      if (component.getName().equals(name))
      {
        return (T) component;
      }
    }

    return null;
  }

  public MBPanel getFirstChildOfPanelWithType(String type)
  {
    ArrayList<MBComponent> result = getChildrenOfKind(MBPanel.class);

    for (MBComponent component : result)
    {
      if (((MBPanel) component).getType().equals(type))
      {
        return (MBPanel) component;
      }
    }

    return null;
  }

  public MBPanel getFirstChildOfPanelWithName(String name)
  {
    ArrayList<MBComponent> result = getChildrenOfKind(MBPanel.class);

    for (MBComponent component : result)
    {
      if (((MBPanel) component).getName().equals(name))
      {
        return (MBPanel) component;
      }
    }

    return null;
  }

  // TODO Method has a selector, needs implementation
  public Object getFirstChildOfKind(Class<?> clazz, Object selector, Object value)
  {
    return null;
  }

  public MBComponentContainer getFirstParentOfKind(Class<?> clazz)
  {
    MBComponentContainer parent = getParent();
    if (parent == null || clazz.isInstance(parent)) return parent;
    else return parent.getFirstParentOfKind(clazz);
  }

  public MBPanel getFirstParentPanelWithType(String type)
  {
    MBPanel parent = (MBPanel) getFirstParentOfKind(MBPanel.class);
    if (parent == null || type.equals(parent.getType())) return parent;
    else return parent.getFirstParentPanelWithType(type);
  }

  // Listener logic is handled by the page; so delegate to parent until the page is reached:
  public void registerValueChangeListener(Object listener, String path)
  {
    getParent().registerValueChangeListener(listener, path);
  }

  public void unregisterValueChangeListener(Object listener)
  {
    getParent().unregisterValueChangeListener(listener);
  }

  public void unregisterValueChangeListener(Object listener, String path)
  {
    getParent().unregisterValueChangeListener(listener, path);
  }

  public boolean notifyValueWillChange(String value, String currentValue, String path)
  {
    return getParent().notifyValueWillChange(value, currentValue, path);
  }

  public void notifyValueChanged(String value, String currentValue, String path)
  {
    getParent().notifyValueChanged(value, currentValue, path);
  }

  public String getName()
  {
    return getDefinition().getName();
  }

}
