FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:pic64gx-curiosity-kit-auth = "file://${MACHINE}.env"

SRC_URI:remove:pic64gx-curiosity-kit-auth = "file://${UBOOT_ENV}.cmd \
                                     file://uEnv.txt \"

do_configure:append:pic64gx-curiosity-kit-auth () {
    cp -f ${WORKDIR}/${MACHINE}.env ${S}/board/microchip/pic64gx_curiosity_kit
}

do_deploy:append:pic64gx-curiosity-kit-auth () {

    if [ ! -f "${HSS_PAYLOAD_KEYDIR}/${HSS_PAYLOAD_PRIVATE_KEYNAME}.pem" ];then
        bbfatal "Authentication Boot file check, missing: ${HSS_PAYLOAD_KEYDIR}/${HSS_PAYLOAD_PRIVATE_KEYNAME}.pem, Refer to the PIC64GX Documentation"
    fi

    if [ ! -f "${UBOOT_SIGN_KEYDIR}/${UBOOT_SIGN_KEYNAME}.crt" ];then
        bbfatal "Authentication Boot file check, missing: ${UBOOT_SIGN_KEYDIR}/${UBOOT_SIGN_KEYNAME}.crt, Refer to the PIC64GX Documentation"
    fi

    if [ ! -f "${UBOOT_SIGN_KEYDIR}/${UBOOT_SIGN_KEYNAME}.key" ];then
        bbfatal "Authentication Boot file check,  missing: ${UBOOT_SIGN_KEYDIR}/${UBOOT_SIGN_KEYNAME}.key, Refer to the PIC64GX Documentation"
    fi

    bbplain "Using Signing Keys Located in ${HSS_PAYLOAD_KEYDIR}"

    hss-payload-generator -c ${WORKDIR}/${HSS_PAYLOAD}.yaml -v ${DEPLOYDIR}/payload.bin -p ${HSS_PAYLOAD_KEYDIR}/${HSS_PAYLOAD_PRIVATE_KEYNAME}.pem
}
