SUMMARY = "libinput"
DESCRIPTION = "libinput is needed for building weston-ivi-shell"
HOMEPAGE = "http://cgit.freedesktop.org/wayland/libinput/"
BUGTRACKER = ""
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=2184aef38ff137ed33ce9a63b9d1eb8f"

DEPENDS = "libevdev udev mtdev"

TAG = "1d0a1d57e2e4549d6b953436d047b29c46336812"
SRC_URI = "git://anongit.freedesktop.org/git/wayland/libinput.git;tag=${TAG}"


S = "${WORKDIR}/git"

inherit autotools pkgconfig

PACKAGECONFIG ??= ""
PACKAGECONFIG[libunwind] = "--with-libunwind,--without-libunwind,libunwind"

FILES_${PN} += "${libdir}/udev/"
FILES_${PN}-dbg += "${libdir}/udev/.debug"                                                
