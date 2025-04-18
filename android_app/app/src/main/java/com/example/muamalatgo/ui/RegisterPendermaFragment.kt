package com.example.muamalatgo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.muamalatgo.backend.RegisterPenderma
import com.example.muamalatgo.databinding.FragmentRegisterPendermaBinding

class RegisterPendermaFragment : Fragment() {

    private var _binding: FragmentRegisterPendermaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterPendermaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegisterPenderma.setOnClickListener {
            val email = binding.inputEmail.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()

            val name = binding.inputNama.text.toString().trim()
            val ic = binding.inputNoKP.text.toString().trim()
            val phone = binding.inputPhone.text.toString().trim()
            val income = binding.inputIncome.text.toString().trim()
            val savings = binding.inputSavings.text.toString().trim()
            val investment = binding.inputInvestment.text.toString().trim()
            val gold = binding.inputGold.text.toString().trim()
            val property = binding.inputProperty.text.toString().trim()
            val profession = binding.inputProfession.text.toString().trim()
            val location = binding.inputLocation.text.toString().trim()
            val donationFreq = binding.inputDonationFreq.text.toString().trim()
            val avgDonation = binding.inputAvgDonation.text.toString().trim()

            // Basic validation
            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || ic.isEmpty()) {
                Toast.makeText(requireContext(), "Sila isi semua medan wajib", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val pendermaData = mapOf(
                "Nama Pemohon/Institusi" to name,
                "Emel" to email,
                "No K/P" to ic,
                "No. Telefon Bimbit" to phone,
                "Declared Income" to income,
                "Savings" to savings,
                "Investment" to investment,
                "Gold" to gold,
                "Property" to property,
                "Profession" to profession,
                "Location" to location,
                "Donation Frequency" to donationFreq,
                "Average Donation Amount" to avgDonation,
                "Fraud Label" to "Unknown"
            )

            RegisterPenderma.registerPendermaUser(
                email = email,
                password = password,
                pendermaData = pendermaData,
                onSuccess = {
                    Toast.makeText(requireContext(), "Pendaftaran berjaya!", Toast.LENGTH_SHORT).show()
                },
                onFailure = { error ->
                    Toast.makeText(requireContext(), "Ralat: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
