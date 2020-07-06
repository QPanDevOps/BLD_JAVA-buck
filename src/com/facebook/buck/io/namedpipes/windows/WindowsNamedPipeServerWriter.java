/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook.buck.io.namedpipes.windows;

import com.facebook.buck.io.namedpipes.NamedPipeWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

/** Server implementation of Windows name pipe writer. */
class WindowsNamedPipeServerWriter extends WindowsNamedPipeServerBase implements NamedPipeWriter {

  public WindowsNamedPipeServerWriter(Path path) {
    super(path);
  }

  @Override
  public OutputStream getOutputStream() throws IOException {
    return connect(WindowsNamedPipeClientWriter.class).getOutputStream();
  }
}