/*
 * Copyright 2018 bundles authors
 * All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.palindromicity.bundles.util;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;

/**
 * Utility functions for dealing with FileObjects.
 */
public class FileUtils {

  /**
   * Ensures that a FileObject that represents a directory actually exists and is readable
   * in the current security context
   * @param dir the FileObject
   * @throws FileSystemException if
   *     * the FileObject is not a directory.
   *     * the FileObject does not exist.
   *     * the FileObject is not readable.
   */
  public static void ensureDirectoryExistAndCanRead(FileObject dir) throws FileSystemException {
    if (dir.exists() && !dir.isFolder()) {
      throw new FileSystemException(dir.getURL() + " is not a directory");
    } else if (!dir.exists()) {
      throw new FileSystemException(dir.getURL() + " does not exist");
    }
    if (!dir.isReadable()) {
      throw new FileSystemException(
          dir.getURL().toString() + " directory does not have read privilege");
    }
  }
}
