/*
 * (C) Copyright Itude Mobile B.V., The Netherlands
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itude.mobile.mobbl.core.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.test.ApplicationTestCase;

import com.itude.mobile.mobbl.core.MBApplicationCore;
import com.itude.mobile.mobbl.core.services.MBMetadataService;
import com.itude.mobile.mobbl.core.util.exceptions.MBInvalidRelativePathException;

public class MBPathUtilTest extends ApplicationTestCase<MBApplicationCore>
{
  public MBPathUtilTest()
  {
    super(MBApplicationCore.class);
  }

  @Override
  protected void setUp() throws Exception
  {
    createApplication();
    MBMetadataService.setConfigName("unittests/config_unittests.xml");
    MBMetadataService.setEndpointsName("testconfig/endpoints.xml");
    super.setUp();
  }

  public void testSplitPathShouldThrow()
  {
    Pattern splitPathPattern = Pattern.compile("/");
    String[] pathsThatThrow = {"../a/b/c", "/../a/b/c", "/a/../../b"};
    List<String> splittedOld = null;
    List<String> splittedNew = null;
    for (String path : pathsThatThrow)
    {
      try
      {
        splittedOld = splitPathOldImplementation(path, splitPathPattern);
        throw new AssertionError("expected an MBInvalidRelativePathException (old) " + path);
      }
      catch (MBInvalidRelativePathException e)
      {
        // expected
      }
      try
      {
        splittedNew = MBPathUtil.splitPath(path);
        throw new AssertionError("expected an MBInvalidRelativePathException (new) " + path);
      }
      catch (MBInvalidRelativePathException e)
      {
        // expected
      }
      compareEqual(splittedOld, splittedNew, path);
    }
  }

  public void testSplitPath()
  {
    Pattern splitPathPattern = Pattern.compile("/");
    String[] pathsToTest = {"a/b/c", "/a/b/c", "a/b/c/", "/a/b/c/", "a/../c", "/a/../c", "a/../c/", "/a/../c/", "a/b/..", "/a/b/..",
        "a/b/../", "/a/b/../", "a///////b/..", "/a/b////////////..", "a/////////b////////../", "////////////a/b/../", "./a/b/c",
        "/./a/b/c", "a/b/./c/", "/a/b/c/.", "/a/b/c/./", "aaaaaaaaaaaaaaa/b/c", "/a/bbbbbbbbbbbbbbbbbbbbbbbbbb/c",
        "a/b/ccccccccccccccccccccccccccc/"};
    List<String> splittedOld = null;
    List<String> splittedNew = null;
    for (String path : pathsToTest)
    {
      splittedOld = splitPathOldImplementation(path, splitPathPattern);
      splittedNew = MBPathUtil.splitPath(path);
      compareEqual(splittedOld, splittedNew, path);
    }
  }

  private static List<String> splitPathOldImplementation(String path, Pattern splitPathPattern)
  {
    List<String> components = new ArrayList<String>();
    String[] splitted = splitPathPattern.split(path, 0);

    for (String component : splitted)
    {
      if (component.equals(".") || component.equals("") || component.equals("/"))
      {
        // Skip
      }
      else if (component.equals(".."))
      {
        if (components.size() == 0)
        {
          throw new MBInvalidRelativePathException(path);
        }
        components.remove(components.size() - 1);
      }
      else
      {
        components.add(component);
      }
    }

    return components;
  }

  private void compareEqual(List<String> splittedOld, List<String> splittedNew, String path)
  {
    assertTrue(path + ", one is null, other is not", (splittedNew == null && splittedOld == null) || splittedNew != null
                                                     && splittedOld != null);
    if (splittedNew == null) return;
    assertTrue(path + ", sizes are different n=" + splittedNew.size() + "o=" + splittedOld.size(), splittedNew.size() == splittedOld.size());
    assertTrue(path + ", values are different", splittedNew.equals(splittedOld));
  }

  @Override
  protected void tearDown() throws Exception
  {
    File cacheDir = new File(getContext().getFilesDir(), "cache");
    if (cacheDir.exists() && cacheDir.isDirectory())
    {
      File[] files = cacheDir.listFiles();
      if (files.length > 0)
      {
        for (File file : files)
          file.delete();
      }
      cacheDir.delete();
    }
    super.tearDown();
  }

}
