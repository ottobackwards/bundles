/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.palindromicity.bundles;

import com.github.palindromicity.bundles.bundle.BundleCoordinates;
import com.github.palindromicity.bundles.bundle.BundleDetails;
import com.github.palindromicity.bundles.util.BundleProperties;
import com.github.palindromicity.bundles.util.BundleUtil;
import com.github.palindromicity.bundles.util.FileSystemManagerFactory;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BundleUtilTest {
    Map<String, String> additionalProperties = new HashMap<>();
    @Test
    public void testManifestWithVersioningAndBuildInfo() throws IOException , URISyntaxException{

        BundleProperties properties = BundleProperties.createBasicBundleProperties("src/test/resources/bundle.properties", additionalProperties);
        // create a FileSystemManager
        FileSystemManager fileSystemManager = FileSystemManagerFactory.createFileSystemManager(new String[] {properties.getArchiveExtension()});

        final FileObject bundleDir = fileSystemManager.resolveFile(BundleProperties.getUri("src/test/resources/utils-bundles/bundle-with-versioning"));
        final BundleDetails bundleDetails = BundleUtil.fromBundleTestDirectory(bundleDir, properties);
        assertEquals(bundleDir.getURL(), bundleDetails.getBundleFile().getURL());

        assertEquals("com.github.palindromicity", bundleDetails.getCoordinates().getGroup());
        assertEquals("palindromicity-hadoop-bundle", bundleDetails.getCoordinates().getId());
        assertEquals("1.2.0", bundleDetails.getCoordinates().getVersion());

        assertEquals("com.github.palindromicity.hadoop", bundleDetails.getDependencyCoordinates().getGroup());
        assertEquals("palindromicity-hadoop-libraries-bundle", bundleDetails.getDependencyCoordinates().getId());
        assertEquals("1.2.1", bundleDetails.getDependencyCoordinates().getVersion());

        assertEquals("FOO", bundleDetails.getBuildBranch());
        assertEquals("1.8.0_74", bundleDetails.getBuildJdk());
        assertEquals("a032175", bundleDetails.getBuildRevision());
        assertEquals("HEAD", bundleDetails.getBuildTag());
        assertEquals("2017-01-23T10:36:27Z", bundleDetails.getBuildTimestamp());
        assertEquals("ottobackwards", bundleDetails.getBuiltBy());
    }

    @Test
    public void testManifestWithoutVersioningAndBuildInfo() throws IOException, URISyntaxException {
        BundleProperties properties = BundleProperties.createBasicBundleProperties("src/test/resources/bundle.properties",  additionalProperties);
        // create a FileSystemManager
        FileSystemManager fileSystemManager = FileSystemManagerFactory.createFileSystemManager(new String[] {properties.getArchiveExtension()});

        final FileObject bundleDir = fileSystemManager.resolveFile(BundleProperties.getUri("src/test/resources/utils-bundles/bundle-without-versioning"));
        final BundleDetails bundleDetails = BundleUtil.fromBundleTestDirectory(bundleDir, properties);
        assertEquals(bundleDir.getURL(), bundleDetails.getBundleFile().getURL());

        Assert.assertEquals(BundleCoordinates.DEFAULT_GROUP, bundleDetails.getCoordinates().getGroup());
        assertEquals("palindromicity-hadoop-bundle", bundleDetails.getCoordinates().getId());
        assertEquals(BundleCoordinates.DEFAULT_VERSION, bundleDetails.getCoordinates().getVersion());

        assertEquals(BundleCoordinates.DEFAULT_GROUP, bundleDetails.getDependencyCoordinates().getGroup());
        assertEquals("palindromicity-hadoop-libraries-bundle", bundleDetails.getDependencyCoordinates().getId());
        assertEquals(BundleCoordinates.DEFAULT_VERSION, bundleDetails.getDependencyCoordinates().getVersion());

        assertNull(bundleDetails.getBuildBranch());
        assertEquals("1.8.0_74", bundleDetails.getBuildJdk());
        assertNull(bundleDetails.getBuildRevision());
        assertNull(bundleDetails.getBuildTag());
        assertNull(bundleDetails.getBuildTimestamp());
        assertEquals("ottobackwards", bundleDetails.getBuiltBy());
    }

    @Test
    public void testManifestWithoutBundleDependency() throws IOException, URISyntaxException {
        BundleProperties properties = BundleProperties.createBasicBundleProperties("src/test/resources/bundle.properties",  additionalProperties);
        // create a FileSystemManager
        FileSystemManager fileSystemManager = FileSystemManagerFactory.createFileSystemManager(new String[] {properties.getArchiveExtension()});

        final FileObject bundleDir = fileSystemManager.resolveFile(BundleProperties.getUri("src/test/resources/utils-bundles/bundle-without-dependency"));
        final BundleDetails bundleDetails = BundleUtil.fromBundleTestDirectory(bundleDir, properties);
        assertEquals(bundleDir.getURL(), bundleDetails.getBundleFile().getURL());

        assertEquals("com.github.palindromicity", bundleDetails.getCoordinates().getGroup());
        assertEquals("palindromicity-hadoop-bundle", bundleDetails.getCoordinates().getId());
        assertEquals("1.2.0", bundleDetails.getCoordinates().getVersion());

        assertNull(bundleDetails.getDependencyCoordinates());

        assertEquals("FOO", bundleDetails.getBuildBranch());
        assertEquals("1.8.0_74", bundleDetails.getBuildJdk());
        assertEquals("a032175", bundleDetails.getBuildRevision());
        assertEquals("HEAD", bundleDetails.getBuildTag());
        assertEquals("2017-01-23T10:36:27Z", bundleDetails.getBuildTimestamp());
        assertEquals("ottobackwards", bundleDetails.getBuiltBy());
    }

    @Test(expected = IOException.class)
    public void testFromManifestWhenBundleDirectoryDoesNotExist() throws IOException, URISyntaxException {
        BundleProperties properties = BundleProperties.createBasicBundleProperties("src/test/resources/bundle.properties", additionalProperties);
        // create a FileSystemManager
        FileSystemManager fileSystemManager = FileSystemManagerFactory.createFileSystemManager(new String[] {properties.getArchiveExtension()});

        final FileObject manifest = fileSystemManager.resolveFile(BundleProperties.getUri("src/test/resources/utils-bundles/bundle-does-not-exist"));
        BundleUtil.fromBundleTestDirectory(manifest, properties );
    }

}
