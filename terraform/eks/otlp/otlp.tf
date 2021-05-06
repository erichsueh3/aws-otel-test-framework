# ------------------------------------------------------------------------
# Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License").
# You may not use this file except in compliance with the License.
# A copy of the License is located at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# or in the "license" file accompanying this file. This file is distributed
# on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
# express or implied. See the License for the specific language governing
# permissions and limitations under the License.
# -------------------------------------------------------------------------

terraform {
  required_providers {
    kubernetes = {
      version = "~> 1.13"
    }
  }
}

locals {
  eks_pod_config = yamldecode(data.template_file.eksconfig.rendered)["sample_app"]
}

# load eks pod config
data "template_file" "eksconfig" {
  template = file(var.eks_pod_config_path)

  vars = {
    data_emitter_image = var.sample_app.image
    testing_id         = var.testing_id
  }
}