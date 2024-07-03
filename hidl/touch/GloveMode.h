/*
 * Copyright (C) 2019-2022 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#pragma once

#include <hidl/MQDescriptor.h>
#include <hidl/Status.h>
#include <vendor/samsung/hardware/touch/1.0/IGloveMode.h>
#include "samsung_touch.h"

namespace vendor {
namespace samsung {
namespace hardware {
namespace touch {
namespace V1_0 {


using ::android::hardware::hidl_array;
using ::android::hardware::hidl_memory;
using ::android::hardware::hidl_string;
using ::android::hardware::hidl_vec;
using ::android::hardware::Return;
using ::android::hardware::Void;
using ::android::sp;

class GloveMode : public IGloveMode {
  public:
    GloveMode() = default;

    bool isSupported();

    // Methods from ::vendor::samsung::hardware::touch::IGloveMode follow.
    Return<bool> isEnabled() override;
    Return<bool> setEnabled(bool enabled) override;

    // Methods from ::android::hidl::base::V1_0::IBase follow.
};

}  // namespace V1_0
}  // namespace touch
}  // namespace hardware
}  // namespace samsung
}  // namespace vendor