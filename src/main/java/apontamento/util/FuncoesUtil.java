package apontamento.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import com.google.common.base.Strings;

public class FuncoesUtil {
	public static String formatarData(Date data, String formato) {
		if(data == null) return "";
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formato);
		LocalDateTime ldt = LocalDateTime.ofInstant(data.toInstant(), ZoneId.systemDefault());
		return dtf.format(ldt);
	}
	
	public static Long qtdMinutosEntreDuasDatas(Date dataInicial, Date dataFinal) {
		LocalDateTime ldt1 = LocalDateTime.ofInstant(dataInicial.toInstant(), ZoneId.systemDefault());
		LocalDateTime ldt2 = LocalDateTime.ofInstant(dataFinal.toInstant(), ZoneId.systemDefault());
		
		return ldt1.until(ldt2, ChronoUnit.MINUTES);
	}
	
	public static LocalDateTime converterStringEmLocalDateTime(String dataHora, String formatoSaida) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formatoSaida);
		return LocalDateTime.parse(dataHora, dtf);
	}
	
	public static String converterLocalDateEmString(LocalDate data, String formatoSaida) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(formatoSaida);
		return dtf.format(data);
	}
	
	public static Date converterLocalDateTimeEmDate(LocalDateTime date) {
		return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date converterLocalDateEmDate(LocalDate date) {
		return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate converterDateEmLocalDate(Date data) {
		return LocalDate.ofInstant(data.toInstant(), ZoneId.systemDefault());
	}
	
	public static String qtdHorasMinutosFormatado(Long totalMinutos) {
		BigDecimal qtdMinutosPorHora = new BigDecimal("60");
		BigDecimal qtdMinutos = totalMinutos == null ? BigDecimal.ZERO : BigDecimal.valueOf(totalMinutos);
		
		BigDecimal totalHoras = BigDecimal.ZERO;
		if(qtdMinutos.compareTo(BigDecimal.ZERO) > 0) {
			totalHoras = qtdMinutos.divide(qtdMinutosPorHora, 1, RoundingMode.HALF_EVEN);
		}
		
		BigDecimal horas = totalHoras.setScale(0, RoundingMode.FLOOR);
		BigDecimal minutos = totalHoras.subtract(horas);
		BigDecimal cem = new BigDecimal("100");
		BigDecimal centesimal  = new BigDecimal("0.6");
		
		minutos = minutos.multiply(cem).setScale(0, RoundingMode.FLOOR);
		minutos = minutos.multiply(centesimal).setScale(0, RoundingMode.FLOOR);
		
		String horarioFormatado = "";
		
		if(horas.compareTo(BigDecimal.ZERO) > 0) {
			horarioFormatado = horas + "h";
		}
		
		if(minutos.compareTo(BigDecimal.ZERO) > 0) {
			horarioFormatado += Strings.isNullOrEmpty(horarioFormatado) ? "" : " ";
			horarioFormatado += minutos + "m";
		}
		
		return horarioFormatado;
	}

	public static Date converterStringEmDate(String data, String formatoSaida) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(formatoSaida);
		return sdf.parse(data);
	}

	public static Date primeiroDiaDoMesCorrente() {
		Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.DATE, 1);
		return calendar.getTime();
	}

	public static Date ultimoDiaDoMesCorrente() {
		Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	public static String converterDateEmString(Date data, String formatoEntrada) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatoEntrada);
		return sdf.format(data);
	}
}
