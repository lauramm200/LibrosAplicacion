package com.example.librosaplicacion

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.librosaplicacion.databinding.ActivityLoginBinding
import com.example.librosaplicacion.databinding.DialogPoliticaPrivacidadBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var modoRegistro = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Pestañas: al seleccionar una, se cambia el modo (posición 1 = Registrarse)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) = cambiarModo(tab.position == 1)
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // El texto inferior solo selecciona la otra pestaña; el listener de arriba hace el resto
        binding.tvCambiarModo.setOnClickListener {
            binding.tabLayout.getTabAt(if (modoRegistro) 0 else 1)?.select()
        }

        binding.tvEnlacePrivacidad.setOnClickListener { mostrarPrivacidad() }

        binding.btnAccion.setOnClickListener {
            if (validar()) irAlMenu()
        }
    }

    // Cambia textos y visibilidad según la pestaña activa
    private fun cambiarModo(registro: Boolean) {
        modoRegistro = registro
        binding.btnAccion.setText(if (registro) R.string.btn_registrarse else R.string.btn_iniciar_sesion)
        binding.tvCambiarModo.setText(if (registro) R.string.cambiar_a_login else R.string.cambiar_a_registro)
        binding.llPrivacidad.visibility = if (registro) View.VISIBLE else View.GONE
    }

    // Comprueba los campos y marca el error en cada uno; devuelve true si todo es correcto
    private fun validar(): Boolean {
        val usuarioOk = binding.etUsuario.text.toString().trim().isNotEmpty()
        val claveOk = binding.etContrasena.text.toString().length >= 6
        val privacidadOk = !modoRegistro || binding.cbPrivacidad.isChecked

        binding.tilUsuario.error = if (usuarioOk) null else getString(R.string.error_usuario_vacio)
        binding.tilContrasena.error = if (claveOk) null else getString(R.string.error_contrasena_corta)
        if (!privacidadOk) Toast.makeText(this, R.string.error_privacidad, Toast.LENGTH_SHORT).show()

        return usuarioOk && claveOk && privacidadOk
    }

    // Diálogo con la política de privacidad (usa dialog_politica_privacidad.xml)
    private fun mostrarPrivacidad() {
        MaterialAlertDialogBuilder(this)
            .setView(DialogPoliticaPrivacidadBinding.inflate(layoutInflater).root)
            .setPositiveButton(R.string.cerrar, null)
            .show()
    }

    // Abre el menú y cierra el login para que el botón "atrás" no vuelva aquí
    private fun irAlMenu() {
        val usuario = binding.etUsuario.text.toString().trim()
        getSharedPreferences("sesion", MODE_PRIVATE).edit().putString("usuario", usuario).apply()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("usuario", usuario)
        startActivity(intent)
        finish()
    }
}