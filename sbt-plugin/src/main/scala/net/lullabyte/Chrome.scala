package net.lullabyte

import chrome.Impl.{App, Background, AppManifest}
import chrome.Manifest
import chrome.permissions.{HostPermission, APIPermission, Permission}
import sbt._

object Chrome {

  def icons(base: String, name: String, sizes: Set[Int]): Map[Int, String] = {
    sizes.map{ size =>
      size -> s"$base/$size/$name"
    }.toMap
  }

  def buildExtentionDirectory(unpacked: File)(manifest: File, jsLib: File,
                                              jsDeps: File, jsLauncher: File, content: File): File =  {
    IO.createDirectory(unpacked)
    IO.copyDirectory(content, unpacked, overwrite = true, preserveLastModified = true)
    IO.copy(List(
      (jsLib, unpacked / "main.js"),
      (jsDeps, unpacked / "deps.js"),
      (jsLauncher, unpacked / "launcher.js"),
      (manifest, unpacked / "manifest.json")
    ), overwrite = true, preserveLastModified = true)
    unpacked
  }

  def generateManifest(out: File)(manifest: Manifest): File = {
    import Pickler._
    val content = upickle.write(manifest)
    IO.write(out, content)
    out
  }
}
