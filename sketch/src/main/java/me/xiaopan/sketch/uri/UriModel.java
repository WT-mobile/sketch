/*
 * Copyright (C) 2017 Peng fei Pan <sky@xiaopan.me>
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

package me.xiaopan.sketch.uri;

import android.content.Context;

import me.xiaopan.sketch.decode.DataSource;
import me.xiaopan.sketch.decode.DecodeException;
import me.xiaopan.sketch.request.DownloadResult;
import me.xiaopan.sketch.request.UriInfo;

public interface UriModel {

    boolean match(String uri);

    String getUriContent(String uri);

    String getDiskCacheKey(String uri);

    boolean isFromNet();

    DataSource getDataSource(Context context, UriInfo uriInfo, DownloadResult downloadResult) throws DecodeException;
}
