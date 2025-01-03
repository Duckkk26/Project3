import ExcelJS from 'exceljs';
import { saveAs } from 'file-saver';

export const exportExcel = async (data, name) => {
  // Tạo workbook mới
  const workbook = new ExcelJS.Workbook();
  const worksheet = workbook.addWorksheet('Sheet1');

  // Tạo tiêu đề cho bảng
  const columns = Object.keys(data[0]).map(key => ({ header: key, key }));
  worksheet.columns = columns;

  // Thêm style cho tiêu đề
  worksheet.getRow(1).eachCell((cell) => {
    cell.font = { bold: true, size: 14 };  // In đậm và font lớn
    cell.alignment = { vertical: 'middle', horizontal: 'center' }; // Căn giữa
    cell.fill = {
      type: 'pattern',
      pattern: 'solid',
      fgColor: { argb: 'FFFF00' },  // Màu nền vàng
    };
    cell.border = {
      top: { style: 'thin' },
      left: { style: 'thin' },
      bottom: { style: 'thin' },
      right: { style: 'thin' },
    };
  });

  // Thêm dữ liệu vào bảng
  data.forEach((row) => {
    const excelRow = worksheet.addRow(Object.values(row));
    excelRow.eachCell((cell) => {
      cell.border = {
        top: { style: 'thin' },
        left: { style: 'thin' },
        bottom: { style: 'thin' },
        right: { style: 'thin' }
      };
      cell.alignment = { vertical: 'middle', horizontal: 'left' }; 
    });
  });

  // Tự động điều chỉnh độ rộng cột theo nội dung và tiêu đề
  worksheet.columns.forEach(column => {
    let maxLength = 20; 
    if (column.header) {
      const headerLength = column.header.length;
      if (headerLength > maxLength) {
        maxLength = headerLength;
      }
    }

    column.eachCell({ includeEmpty: true }, (cell) => {
      const columnLength = cell.value ? cell.value.toString().length : 20;
      if (columnLength > maxLength) {
        maxLength = columnLength;
      }
    });
    column.width = maxLength < 20 ? 20 : maxLength; 
  });

  // Ghi workbook vào file
  const buffer = await workbook.xlsx.writeBuffer();
  const blob = new Blob([buffer], { type: 'application/octet-stream' });
  saveAs(blob, `${name}.xlsx`);
};
