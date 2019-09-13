// Copyright 2004-present Facebook. All Rights Reserved.

package org.pytorch;

import com.facebook.jni.HybridData;

/**
 * Java holder for torch::jit::script::Module which owns it on jni side.
 */
public class Module {

  private NativePeer mNativePeer;

  /**
   * Loads serialized to file torchscript module from specified absolute path on the disk.
   *
   * @param modelAbsolutePath absolute path to file that contains serialized torchscript module.
   * @return new {@link org.pytorch.Module} object which owns torch::jit::script::Module on jni
   * side.
   */
  public static Module load(final String modelAbsolutePath) {
    return new Module(modelAbsolutePath);
  }

  private Module(final String moduleAbsolutePath) {
    this.mNativePeer = new NativePeer(moduleAbsolutePath);
  }

  /**
   * Runs 'forward' method of loaded torchscript module with specified arguments.
   *
   * @param inputs arguments for torchscript module 'forward' method.
   * @return result of torchscript module 'forward' method evaluation
   */
  public IValue forward(IValue... inputs) {
    return mNativePeer.forward(inputs);
  }

  /**
   * Runs 'forward' method of loaded torchscript module with specified arguments.
   *
   * @param methodName torchscript module method to run
   * @param inputs     arguments that will be specified to torchscript module method call
   * @return result of torchscript module specified method evaluation
   */
  public IValue runMethod(String methodName, IValue... inputs) {
    return mNativePeer.runMethod(methodName, inputs);
  }

  private static class NativePeer {
    static {
      System.loadLibrary("pytorch");
    }

    private final HybridData mHybridData;

    private static native HybridData initHybrid(String moduleAbsolutePath);

    NativePeer(String moduleAbsolutePath) {
      mHybridData = initHybrid(moduleAbsolutePath);
    }

    private native IValue forward(IValue... inputs);

    private native IValue runMethod(String methodName, IValue... inputs);
  }
}
