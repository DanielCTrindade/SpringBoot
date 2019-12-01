package curso.springboot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Carro;
import curso.springboot.model.Despesa;
import curso.springboot.repository.CarroRepository;
import curso.springboot.repository.DespesaRepository;

@Controller
public class CarroController {
	
	@Autowired
	private CarroRepository carroRepository;
	
	@Autowired
	private DespesaRepository despesaRepository;
	
	@Autowired
	private ReportUtil reportUtil;
	
	@Autowired
	private ReportUtil reportUtil2;
	
	@RequestMapping(method= RequestMethod.GET, value="/cadastrocarro")
	public ModelAndView inicio() {
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastrocarro");
		modelAndView.addObject("carroobj",new Carro());
		Iterable<Carro> carrosIt = carroRepository.findAll();
		modelAndView.addObject("carros", carrosIt);
		return modelAndView;
		
	}
	
	@RequestMapping(method=RequestMethod.POST, value="**/salvarcarro")
	public ModelAndView salvar(@Valid Carro carro, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastrocarro");
			Iterable<Carro> carrosIt = carroRepository.findAll();
			modelAndView.addObject("carros", carrosIt);
			modelAndView.addObject("carroobj",carro);
			
			List<String> msg = new ArrayList<String>();
			for(ObjectError objectError : bindingResult.getAllErrors()) {
				msg.add(objectError.getDefaultMessage());
			}
			modelAndView.addObject("msg",msg);
			return modelAndView;
		}
		carroRepository.save(carro);
		ModelAndView andView = new ModelAndView("cadastro/cadastrocarro");
		Iterable<Carro> carrosIt = carroRepository.findAll();
		andView.addObject("carros", carrosIt);
		andView.addObject("carroobj",new Carro());
		
		return andView;
		
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/listacarros")
	public ModelAndView carros() {
		ModelAndView andView = new ModelAndView("cadastro/cadastrocarro");
		Iterable<Carro> carrosIt = carroRepository.findAll();
		andView.addObject("carros", carrosIt);
		andView.addObject("carroobj", new Carro());
		return andView;
	}
	
	@GetMapping("/editarcarro/{idcarro}")
	public ModelAndView editar(@PathVariable("idcarro") Long idcarro) {

		ModelAndView modelAndView = new ModelAndView("cadastro/cadastrocarro");
		Optional<Carro> carro = carroRepository.findById(idcarro);
		modelAndView.addObject("carroobj", carro.get());
		return modelAndView;

	}

	@GetMapping("/removercarro/{idcarro}")
	public ModelAndView excluir(@PathVariable("idcarro") Long idcarro) {

		carroRepository.deleteById(idcarro);
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastrocarro");
		modelAndView.addObject("carroobj", new Carro());
		modelAndView.addObject("carros", carroRepository.findAll());
		return modelAndView;

	}
	
	@PostMapping("**/pesquisarcarro")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastrocarro");
		modelAndView.addObject("carros", carroRepository.findCarroByName(nomepesquisa));
		modelAndView.addObject("carroobj",new Carro());
		return modelAndView;
		
	}
	@GetMapping("**/pesquisarcarro")
	public void imprimePDF(@RequestParam("nomepesquisa") String nomepesquisa,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
			List<Despesa> despesas = new ArrayList<Despesa>();
			
			if(nomepesquisa !=null && !nomepesquisa.isEmpty()) {
				despesas = despesaRepository.findDespesaByName(nomepesquisa);
			}else {
				
				Iterable<Despesa> iterator = despesaRepository.findAll();
				for(Despesa despesa: iterator) {
					despesas.add(despesa);
				}
			}
			byte[] pdf = reportUtil.gerarRelatorio(despesas, "Relatório", request.getServletContext());
			response.setContentLength(pdf.length);
			response.setContentType("application/octet-stream");
			
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", "Relatório de despesas.pdf");
			response.setHeader(headerKey, headerValue);
			response.getOutputStream().write(pdf);
	}
	
	@GetMapping("/despesas/{idcarro}")
	public ModelAndView despesas(@PathVariable("idcarro") Long idcarro) {
		
		ModelAndView modelAndView =new ModelAndView ("cadastro/despesas");
		Optional<Carro> carro = carroRepository.findById(idcarro);
		modelAndView.addObject("carroobj", carro.get());
		modelAndView.addObject("despesas",despesaRepository.getDespesas(idcarro));
		return modelAndView;
		
	}
	
	@PostMapping("**/addDespesaCarro/{carroid}")
	public ModelAndView addDespesaCarro(Despesa despesa, @PathVariable("carroid") Long carroid) {
		
		Carro carro= carroRepository.findById(carroid).get();
		
		despesa.setCarro(carro);
		despesaRepository.save(despesa);
		ModelAndView modelAndView = new ModelAndView("cadastro/despesas");
		modelAndView.addObject("carroobj",carro);
		modelAndView.addObject("despesas",despesaRepository.getDespesas(carroid));
		
		return modelAndView;
	}
	
	@GetMapping("/removerdespesa/{iddespesa}")
	public ModelAndView removerdespesa(@PathVariable("iddespesa") Long iddespesa) {
		
		Carro carro = despesaRepository.findById(iddespesa).get().getCarro();
		despesaRepository.deleteById(iddespesa);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/despesas");
		modelAndView.addObject("carroobj",carro);
		modelAndView.addObject("despesas",despesaRepository.getDespesas(carro.getId()));
		return modelAndView;

	}
	
	@PostMapping("**/pesquisardespesa")
	public ModelAndView pesquisarDespesa(@RequestParam("nomepesquisa") String nomepesquisa) {
		ModelAndView modelAndView = new ModelAndView("cadastro/despesas");
		modelAndView.addObject("despesas",despesaRepository.findDespesaByName(nomepesquisa));
		modelAndView.addObject("carroobj",new Carro());
		return modelAndView;
	}
	

}
