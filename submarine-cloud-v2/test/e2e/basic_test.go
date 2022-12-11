/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package e2e

import (
	"context"
	"fmt"
	"testing"

	"github.com/apache/submarine/submarine-cloud-v2/pkg/apis/submarine/v1alpha1"
	operatorFramework "github.com/apache/submarine/submarine-cloud-v2/test/e2e/framework"
	corev1 "k8s.io/api/core/v1"
	apierrors "k8s.io/apimachinery/pkg/api/errors"
	metav1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	"k8s.io/apimachinery/pkg/util/wait"

	"github.com/stretchr/testify/assert"
)

// create & delete submarine custom resource with yaml
func TestSubmitSubmarineCustomResourceYaml(t *testing.T) {
	t.Log("[TestSubmitSubmarineCustomResourceYaml]")

	// create a test namespace
	submarineNs := "submarine-test-submit-custom-resource"
	t.Logf("[Creating] Namespace %s", submarineNs)
	_, err := framework.KubeClient.CoreV1().Namespaces().Create(context.TODO(), &corev1.Namespace{
		ObjectMeta: metav1.ObjectMeta{
			Name: submarineNs,
		},
	}, metav1.CreateOptions{})
	assert.Equal(t, nil, err)

	submarine, err := operatorFramework.MakeSubmarineFromYaml("../../artifacts/examples/example-submarine.yaml")
	assert.Equal(t, nil, err)

	// create submarine
	submarineName := submarine.Name
	t.Logf("[Creating] Submarine %s/%s", submarineNs, submarineName)
	err = operatorFramework.CreateSubmarine(framework.SubmarineClient, submarineNs, submarine)
	assert.Equal(t, nil, err)

	// wait for submarine to be in RUNNING state
	state := GetJobState(t, submarineNs, submarineName)
	err = wait.Poll(INTERVAL, TIMEOUT, func() (done bool, err error) {
		if state.State == v1alpha1.RunningState {
			return true, nil
		}
		if state.State == v1alpha1.FailedState {
			return true, fmt.Errorf("fail to create submarine %s/%s: %s", submarineNs, submarineName, state.ErrorMessage)
		}

		state = GetJobState(t, submarineNs, submarineName)

		return false, nil
	})
	assert.Equal(t, nil, err)

	// delete submarine
	t.Logf("[Deleting] Submarine %s/%s", submarineNs, submarineName)
	err = operatorFramework.DeleteSubmarine(framework.SubmarineClient, submarineNs, submarineName)
	assert.Equal(t, nil, err)

	// wait for submarine to be deleted entirely
	_, getError := operatorFramework.GetSubmarine(framework.SubmarineClient, submarineNs, submarineName)
	err = wait.Poll(INTERVAL, TIMEOUT, func() (done bool, err error) {
		if apierrors.IsNotFound(getError) {
			return true, nil
		}
		_, getError = operatorFramework.GetSubmarine(framework.SubmarineClient, submarineNs, submarineName)
		return false, nil
	})
	assert.Equal(t, nil, err)

	// delete the test namespace
	t.Logf("[Deleting] Namespace %s", submarineNs)
	err = framework.KubeClient.CoreV1().Namespaces().Delete(context.TODO(), submarineNs, metav1.DeleteOptions{})
	assert.Equal(t, nil, err)
}
