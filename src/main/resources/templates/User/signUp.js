class FormValidator {
    constructor(form, fields) {
        this.form = form
        this.fields = fields
    }

    initialize() {
        this.validateOnEntry()
        this.validateOnSubmit()
    }


    //
    // validateOnEntry() {
    //     let self = this
    //     this.fields.forEach(field => {
    //         const input = document.querySelector(`#${field}`)
    //
    //         input.addEventListener('input', () => {
    //             self.validateFields(input)
    //         })
    //     })
    // }

    validateFields(field) {

        if (field.value.trim() === "") {
            this.setStatus(field, null, "error")
        } else {
            this.setStatus(field, null, "success")
        }

        if (field.id === "password_confirmation") {
            const passwordField = this.form.querySelector('#password')

            if (field.value.trim() === "") {
                this.setStatus(field, "Password confirmation required", "error")
            } else if (field.value !== passwordField.value) {
                this.setStatus(field, "Password does not match", "error")
            } else {
                this.setStatus(field, null, "success")
            }
        }
    }

    setStatus(field, message, status) {
        const successIcon = field.parentElement.querySelector('.icon-success')
        const errorIcon = field.parentElement.querySelector('.icon-error')

        if (status === "success") {
            if (errorIcon) {
                errorIcon.classList.add('hidden')
            }

            successIcon.classList.remove('hidden')
            field.classList.remove('input-error')
        }

        if (status === "error") {
            if (successIcon) {
                successIcon.classList.add('hidden')
            }

            errorIcon.classList.remove('hidden')
            field.classList.add('input-error')
        }
    }
}

const form = document.querySelector('#form')
const fields = ["firstName", "lastName","nickname","birthday","password", "password_confirmation"]
const validator = new FormValidator(form, fields)
validator.initialize()
