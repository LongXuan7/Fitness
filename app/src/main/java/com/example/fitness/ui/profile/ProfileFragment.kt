package com.example.fitness.ui.profile

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.fitness.R
import com.example.fitness.databinding.FragmentProfileBinding
import com.example.fitness.ui.login.LoginActivity
import com.example.fitness.ui.setting.SettingActivity
import com.example.fitness.util.base.BaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    override val viewModel: ProfileViewModel
            by viewModel()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }

    override fun setUpViews() {
        firebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        loadUserInfo()
    }

    override fun setupObservers() {

    }

    override fun setUpOnClick() {
        binding.textView38.setOnClickListener {
            startActivity(Intent(requireContext(), SettingActivity::class.java))
        }
        binding.textView40.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
                firebaseAuth.signOut()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().finish()
            }
        }
    }

    private fun loadUserInfo() {
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (account != null) {
            val displayName = account.displayName ?: "No Name"
            val photoUrl = account.photoUrl

            binding.textView18.text = displayName
            Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.ronaldo)
                .into(binding.imageView13)
        }
    }

}