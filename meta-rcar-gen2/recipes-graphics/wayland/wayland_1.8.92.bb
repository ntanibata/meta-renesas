SUMMARY = "Wayland, a protocol between a compositor and clients"
DESCRIPTION = "Wayland is a protocol for a compositor to talk to its clients \
as well as a C library implementation of that protocol. The compositor can be \
a standalone display server running on Linux kernel modesetting and evdev \
input devices, an X application, or a wayland client itself. The clients can \
be traditional applications, X servers (rootless or fullscreen) or other \
display servers."
HOMEPAGE = "http://wayland.freedesktop.org"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=b31d8f53b6aaf2b4985d7dd7810a70d1" 

SRC_URI = "http://wayland.freedesktop.org/releases/${BPN}-${PV}.tar.xz"
SRC_URI[md5sum] = "697e5f1ee4727971ea1fd4dde386c179"
SRC_URI[sha256sum] = "0f1507dbe499198eec7517572113a6d14ba2bb38eba1f5ba6b1e1aac35372039"

EXTRA_OECONF_class-native = "--disable-documentation"

inherit autotools pkgconfig

# We need wayland-native for the wayland-scanner utility
BBCLASSEXTEND = "native"

DEPENDS_class-native = "expat-native libffi-native"
DEPENDS = "expat libffi wayland-native"

EXTRA_OECONF = "--disable-documentation --with-host-scanner"

# Wayland installs a M4 macro for other projects to use, which uses the target
# pkg-config to find files.  Replace pkg-config with pkg-config-native.
do_install_append_class-native() {
  sed -e 's,PKG_CHECK_MODULES(.*),,g' \
      -e 's,$PKG_CONFIG,pkg-config-native,g' \
      -i ${D}/${datadir}/aclocal/wayland-scanner.m4
}

sysroot_stage_all_append_class-target () {
        cp ${STAGING_DATADIR_NATIVE}/aclocal/wayland-scanner.m4 ${SYSROOT_DESTDIR}/${datadir}/aclocal/
}
