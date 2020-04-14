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

package com.facebook.buck.downwardapi;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.facebook.buck.downward.model.ChromeTraceEvent;
import com.facebook.buck.downward.model.ConsoleEvent;
import com.facebook.buck.downward.model.EventTypeMessage;
import com.facebook.buck.downward.model.LogEvent;
import com.facebook.buck.downward.model.StepEvent;
import com.google.protobuf.AbstractMessage;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** Json implementation of Downward API Protocol. */
enum JsonDownwardProtocol implements DownwardProtocol {
  INSTANCE;

  private final JsonFormat.Parser parser = JsonFormat.parser();
  private final JsonFormat.Printer printer = JsonFormat.printer();

  @Override
  public void write(AbstractMessage message, OutputStream outputStream) throws IOException {
    String json = printer.print(message);
    byte[] bytes = json.getBytes(UTF_8);
    // write json length
    outputStream.write(String.valueOf(bytes.length).getBytes(UTF_8));
    // write delimiter
    DownwardProtocolUtils.writeDelimiter(outputStream);
    // write json
    outputStream.write(bytes);
  }

  @Override
  public EventTypeMessage.EventType readEventType(InputStream inputStream) throws IOException {
    String json = readJsonObjectAsString(inputStream);
    Message.Builder builder = EventTypeMessage.newBuilder();
    parser.merge(json, builder);
    return ((EventTypeMessage) builder.build()).getEventType();
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends AbstractMessage> T readEvent(
      InputStream inputStream, EventTypeMessage.EventType eventType) throws IOException {
    String json = readJsonObjectAsString(inputStream);
    Message.Builder builder = getMessageBuilder(eventType);
    parser.merge(json, builder);
    return (T) builder.build();
  }

  private String readJsonObjectAsString(InputStream inputStream) throws IOException {
    int length = DownwardProtocolUtils.readFromStream(inputStream, Integer::parseInt);
    byte[] buffer = new byte[length];
    int bytesRead = inputStream.read(buffer);
    if (bytesRead != length) {
      throw new IOException(
          "Expected to read "
              + length
              + " bytes, but it was read only "
              + bytesRead
              + " bytes instead.");
    }
    return new String(buffer, UTF_8);
  }

  private Message.Builder getMessageBuilder(EventTypeMessage.EventType eventType) {
    switch (eventType) {
      case CONSOLE_EVENT:
        return ConsoleEvent.newBuilder();
      case LOG_EVENT:
        return LogEvent.newBuilder();
      case STEP_EVENT:
        return StepEvent.newBuilder();
      case CHROME_TRACE_EVENT:
        return ChromeTraceEvent.newBuilder();

      case UNKNOWN:
      case UNRECOGNIZED:
      default:
        throw new IllegalStateException("Unexpected value: " + eventType);
    }
  }
}