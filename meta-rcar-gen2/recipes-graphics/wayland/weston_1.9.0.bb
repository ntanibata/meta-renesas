SUMMARY = "Weston, a Wayland compositor"
DESCRIPTION = "Weston is the reference implementation of a Wayland compositor"
HOMEPAGE = "https://github.com/ntanibata/weston-ivi-shell"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=d79ee9e66bb0f95d3386a7acae780b70"

SRC_URI = "http://wayland.freedesktop.org/releases/${BPN}-${PV}.tar.xz \
           file://0001-ivi-shell-implement-multiscreen-feature.patch \
           file://0001-gl-renderer-for-renesas-board.patch \
           file://0002-compositor-for-renesas-board.patch " 
SRC_URI[md5sum] = "66bbba12f546570b4d97f676bc79a28e"
SRC_URI[sha256sum] = "9c1b03f3184fa0b0dfdf67e215048085156e1a2ca344af6613fed36794ac48cf"

inherit autotools pkgconfig useradd

DEPENDS = "libxkbcommon gdk-pixbuf pixman cairo glib-2.0 jpeg"
DEPENDS += "wayland libinput virtual/egl pango"

EXTRA_OECONF = "--enable-setuid-install \
                --disable-xwayland \
                --enable-simple-clients \
                --enable-clients \
                --enable-demo-clients-install \
                --disable-rpi-compositor \
                --disable-rdp-compositor \
                "

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'kms fbdev wayland egl', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'launch', '', d)} \
                  "
#
# Compositor choices
#
# Weston on KMS
PACKAGECONFIG[kms] = "--enable-drm-compositor, --disable-drm-compositor udev virtual/mesa mtdev"
# Weston on Wayland (nested Weston)
PACKAGECONFIG[wayland] = "--enable-wayland-compositor,--disable-wayland-compositor,virtual/mesa"
# Weston on X11
PACKAGECONFIG[x11] = "--enable-x11-compositor,--disable-x11-compositor,virtual/libx11 libxcb libxcb libxcursor cairo"
# Headless Weston
PACKAGECONFIG[headless] = "--enable-headless-compositor,--disable-headless-compositor"
# Weston on framebuffer
PACKAGECONFIG[fbdev] = "--enable-fbdev-compositor,--disable-fbdev-compositor,udev mtdev"
# weston-launch
PACKAGECONFIG[launch] = "--enable-weston-launch,--disable-weston-launch,libpam drm"
# VA-API desktop recorder
PACKAGECONFIG[vaapi] = "--enable-vaapi-recorder,--disable-vaapi-recorder,libva"
# Weston with EGL support
PACKAGECONFIG[egl] = "--enable-egl --enable-simple-egl-clients,--disable-egl --disable-simple-egl-clients,virtual/egl"
# Weston with cairo glesv2 support
PACKAGECONFIG[cairo-glesv2] = "--with-cairo-glesv2,--with-cairo=image,cairo"
# Weston with lcms support
PACKAGECONFIG[lcms] = "--enable-lcms,--disable-lcms,lcms"
# Weston with webp support
PACKAGECONFIG[webp] = "--enable-webp,--disable-webp,libwebp"

do_install_append() {
	# Weston doesn't need the .la files to load modules, so wipe them
	rm -f ${D}/${libdir}/weston/*.la

	# If X11, ship a desktop file to launch it
	if [ "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)}" = "x11" ]; then
		install -d ${D}${datadir}/applications
		install ${WORKDIR}/weston.desktop ${D}${datadir}/applications

		install -d ${D}${datadir}/icons/hicolor/48x48/apps
		install ${WORKDIR}/weston.png ${D}${datadir}/icons/hicolor/48x48/apps
        fi
}

PACKAGES += "${PN}-examples"

FILES_${PN} = "${bindir}/weston ${bindir}/weston-terminal ${bindir}/weston-info ${bindir}/weston-launch ${bindir}/wcap-decode ${libexecdir} ${datadir}"
FILES_${PN}-examples = "${bindir}/*"

RDEPENDS_${PN} += "xkeyboard-config"
RRECOMMENDS_${PN} = "liberation-fonts"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "--system weston-launch"
