package com.AppRH.AppRH.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AppRH.AppRH.models.Candidato;
import com.AppRH.AppRH.models.vaga;
import com.AppRH.AppRH.repository.CondidatoRepository;
import com.AppRH.AppRH.repository.VagaRepository;

@Controller
public class VagaController {

	@Autowired
	private VagaRepository vr;
	@Autowired
	private CondidatoRepository cr;

	// cadastra vaga
	@RequestMapping(value = "/cadastrarVaga", method = RequestMethod.GET)
	public String form() {
		return "vaga/formVaga";
	}

	@RequestMapping(value = "/cadastrarVaga", method = RequestMethod.POST)
	public String form(@Valid vaga vaga, BindingResult result, RedirectAttributes attributes) {

		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos...");
			return "redirect:/cadastrarVaga";
		}

		vr.save(vaga);
		attributes.addFlashAttribute("mensagem", "Vaga cadastrada com sucesso");
		return "redirect:/cadastrarVaga";
	}

	// Lista vagas

	@RequestMapping("/vagas")
	public ModelAndView listaVagas() {
		ModelAndView mv = new ModelAndView("vaga/listaVaga");
		Iterable<vaga> vagas = vr.findAll();
		mv.addObject("vagas", vagas);
		return mv;
	}

	//
	@RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
	public ModelAndView detalhesVaga(@PathVariable("codigo") long codigo) {
		vaga vaga = vr.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("vaga/detalhesVaga");
		mv.addObject("vaga", vaga);

		Iterable<Candidato> candidatos = cr.findByVaga(vaga);
		mv.addObject("candidatos", candidatos);
		return mv;
	}

	// Metodo que deleta a vaga
	@RequestMapping("/deletarVaga")
	public String deletarVaga(long codigo) {
		vaga vaga = vr.findByCodigo(codigo);
		vr.delete(vaga);
		return "redirect:/vagas";
	}

	@RequestMapping(value = "/{codigo}", method = RequestMethod.POST)
	public String detalhesVagasPost(@PathVariable("codigo") long codigo, @Valid Candidato candidato,
			BindingResult result, RedirectAttributes attributes) {
		
			if(result.hasErrors()) {
				attributes.addFlashAttribute("mensagem", "Verifique os campos...");
				return "redirect:/{codigo}";
				
			}
			
			//Teste de rg dublicado
			
			if(cr.findByRg(candidato.getRg()) != null) {
				attributes.addFlashAttribute("mensagem_erro", "Rg duplicado");
				return "redirect:/{codigo}";
			}
			
			vaga vaga = vr.findByCodigo(codigo);
			candidato.setVaga(vaga);
			cr.save(candidato);
			attributes.addFlashAttribute("mensagem", "Candidato adicionado com sucesso");
			
			return "redirect:/{codigo}";
		}
	
	//Deleta Cadidato
	@RequestMapping("/deletarCandidato")
	public String deletarCandidato(String rg) {
		Candidato candidato = cr.findByRg(rg);
		vaga vaga = candidato.getVaga();
		String codigo = "" + vaga.getCodigo();
		
		cr.delete(candidato);
		return "redirect:/" + codigo;
	}
	
	//Metodo que atualiza as vagas
	
	//formulario edição de vaga
	
	@RequestMapping(value = "/editar-vaga", method = RequestMethod.GET)

	public ModelAndView editarVaga(long codigo) {
		vaga vaga = vr.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("vaga/update-vaga");
		mv.addObject("vaga", vaga);
		return mv;
	}
	
	//Update da vaga
	@RequestMapping(value="editar-vaga", method = RequestMethod.POST)
	public String updateVaga(@Valid vaga vaga, BindingResult result, RedirectAttributes attributes) {
		vr.save(vaga);
		attributes.addFlashAttribute("sucess", "Vaga alterada com sucesso!");
		
		long codigoLong = vaga.getCodigo();
		String codigo = ""  +codigoLong;
		return "redirect:/" + codigo;
	}
	
}
