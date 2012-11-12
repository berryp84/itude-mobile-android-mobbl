package com.itude.mobile.mobbl2.client.core.view.builders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.itude.mobile.mobbl2.client.core.MBException;
import com.itude.mobile.mobbl2.client.core.util.AssertUtil;
import com.itude.mobile.mobbl2.client.core.util.ComparisonUtil;
import com.itude.mobile.mobbl2.client.core.view.MBComponent;

public class MBBuilderRegistry<T extends MBComponent, S>
{

  private Set<Filter<S>>              _builders;

  private Map<String, Map<String, S>> _searchOptimizer;

  public MBBuilderRegistry()
  {
    _builders = new HashSet<MBBuilderRegistry.Filter<S>>();
  }

  void registerBuilder(String type, S builder)
  {
    AssertUtil.notNull("builder", builder);

    registerBuilder(type, null, builder);
  }

  void registerBuilder(String type, String style, S builder)
  {
    AssertUtil.notNull("builder", builder);

    _builders.add(new Filter<S>(builder, type, style));
    _searchOptimizer = null;
  }

  public S getBuilder(String type, String style)
  {
    if (_searchOptimizer == null) buildSearchOptimizer();

    Map<String, S> styleMap = _searchOptimizer.get(type);
    if (styleMap == null) styleMap = _searchOptimizer.get(null);
    if (styleMap == null) throw new MBException("No builder found for type " + type + " and style " + style);

    S builder = styleMap.get(style);
    if (builder == null) builder = styleMap.get(null);
    if (builder == null) throw new MBException("No builder found for type " + type + " and style " + style);
    
    return builder;
  }

  private void buildSearchOptimizer()
  {
    Map<String, Map<String, S>> typeMap = new HashMap<String, Map<String, S>>();
    for (Filter<S> filter : _builders)
    {
      Map<String, S> styleMap = typeMap.get(filter.getType());
      if (styleMap == null)
      {
        styleMap = new HashMap<String, S>();
        typeMap.put(filter.getType(), styleMap);
      }
      styleMap.put(filter.getStyle(), filter.getBuilder());
    }

    _searchOptimizer = typeMap;

  }

  private static class Filter<S>
  {
    private final String _type;
    private final String _style;
    private final S      _builder;

    public Filter(S builder, String type, String style)
    {
      _type = type;
      _style = style;
      _builder = builder;
    }

    public String getType()
    {
      return _type;
    }

    public String getStyle()
    {
      return _style;
    }

    public S getBuilder()
    {
      return _builder;
    }

    @Override
    public boolean equals(Object o)
    {
      if (o == null) return false;
      if (!(o instanceof Filter<?>)) return false;
      Filter<?> other = (Filter<?>) o;
      return ComparisonUtil.safeEquals(_type, other._type) && ComparisonUtil.safeEquals(_style, other._style);
    }

    @Override
    public int hashCode()
    {
      String hashString = "";
      if (_type != null) hashString += _type;
      if (_style != null) hashString += _style;
      return hashString.hashCode();
    }
  }

}