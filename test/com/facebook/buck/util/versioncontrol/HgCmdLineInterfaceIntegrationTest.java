 * Copyright (c) Facebook, Inc. and its affiliates.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
import static org.junit.Assert.assertFalse;
    assertFalse(repoThreeCmdLine.diffBetweenRevisions("adf7a0", "adf7a0").isPresent());
        repoThreeCmdLine.diffBetweenRevisions("b1fd7e", "2911b3").get().get()) {