FILESEXTRAPATHS:append := "${THISDIR}/${PN}:"
SRC_URI:append = " file://updatecommon-session"

do_install:append() {
   cp -f ${WORKDIR}/updatecommon-session ${D}${sysconfdir}/pam.d/common-session                                         
}

